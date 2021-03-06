/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.addon.parser.java.ui;

import org.jboss.forge.parser.java.JavaEnum;
import org.jboss.forge.parser.java.JavaSource;

/**
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public class JavaEnumCommand extends AbstractJavaSourceCommand
{
   @Override
   protected Class<? extends JavaSource<?>> getSourceType()
   {
      return JavaEnum.class;
   }

   @Override
   protected String getType()
   {
      return "Enum";
   }
}
