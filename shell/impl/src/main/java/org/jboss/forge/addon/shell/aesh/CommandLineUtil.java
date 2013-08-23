/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.shell.aesh;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.aesh.cl.CommandLine;
import org.jboss.aesh.cl.CommandLineParser;
import org.jboss.aesh.cl.ParserBuilder;
import org.jboss.aesh.cl.builder.OptionBuilder;
import org.jboss.aesh.cl.exception.OptionParserException;
import org.jboss.aesh.cl.internal.CommandInt;
import org.jboss.aesh.cl.internal.OptionInt;
import org.jboss.forge.addon.convert.ConverterFactory;
import org.jboss.forge.addon.shell.util.ShellUtil;
import org.jboss.forge.addon.ui.UICommand;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.input.ManyValued;
import org.jboss.forge.addon.ui.input.SingleValued;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.util.InputComponents;

/**
 * Contains utility methods to parse command lines
 * 
 * @author <a href="mailto:stale.pedersen@jboss.org">Ståle W. Pedersen</a>
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public class CommandLineUtil
{
   private static final Logger logger = Logger.getLogger(CommandLineUtil.class.getName());

   private ConverterFactory converterFactory;

   public CommandLineUtil(ConverterFactory converterFactory)
   {
      this.converterFactory = converterFactory;
   }

   public CommandLineParser generateParser(UICommand command,
            Map<String, InputComponent<?, Object>> inputs)
   {
      ParserBuilder builder = new ParserBuilder();

      UICommandMetadata metadata = command.getMetadata();
      CommandInt parameter = new CommandInt(ShellUtil.shellifyName(metadata.getName()), metadata.getDescription());

      for (InputComponent<?, Object> input : inputs.values())
      {
         Object defaultValue = InputComponents.getValueFor(input);
         boolean isMultiple = input instanceof ManyValued;
         boolean hasValue = (InputComponents.getInputType(input) != InputType.CHECKBOX && !Boolean.class
                  .isAssignableFrom(input.getValueType()));
         try
         {
            OptionBuilder optionBuilder = new OptionBuilder();

            optionBuilder.name(input.getName())
                     .addDefaultValue(defaultValue == null ? null : defaultValue.toString())
                     .description(input.getLabel())
                     .hasMultipleValues(isMultiple)
                     .hasValue(hasValue)
                     .required(input.isRequired());

            if (input.getShortName() != InputComponents.DEFAULT_SHORT_NAME)
            {
               optionBuilder.shortName(input.getShortName());
            }
            OptionInt option = optionBuilder.create();
            if (input.getName().equals("arguments"))
            {
               parameter.setArgument(option);
            }
            else
            {
               parameter.addOption(option);
            }
         }
         catch (OptionParserException e)
         {
            logger.log(Level.SEVERE, "Error while parsing command option", e);
         }
      }
      builder.parameter(parameter);
      return builder.generateParser();
   }

   public void populateUIInputs(CommandLine commandLine,
            Map<String, InputComponent<?, Object>> inputs)
   {
      for (InputComponent<?, Object> input : inputs.values())
      {
         if (input.getName().equals("arguments"))
         {
            InputComponents.setValueFor(converterFactory, input, commandLine.getArgument().getValue());
         }
         else if (input instanceof ManyValued)
         {
            InputComponents.setValueFor(converterFactory, input, commandLine.getOptionValues(input.getName()));
         }
         else if (input instanceof SingleValued)
         {
            InputComponents.setValueFor(converterFactory, input, commandLine.getOptionValue(input.getName()));
         }
      }
   }
}
