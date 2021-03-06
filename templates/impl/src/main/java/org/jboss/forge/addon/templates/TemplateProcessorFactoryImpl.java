/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.addon.templates;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.furnace.services.Imported;
import org.jboss.forge.furnace.util.Assert;

/**
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
@Singleton
public class TemplateProcessorFactoryImpl implements TemplateProcessorFactory
{
   @Inject
   private Imported<TemplateGenerator> generators;

   @Override
   public TemplateProcessor fromTemplate(Template template)
   {
      Assert.notNull(template, "Template resource cannot be null");
      Assert.isTrue(template.exists(), "Template does not exist: " + template);
      for (TemplateGenerator generator : generators)
      {
         if (generator.handles(template))
         {
            return new TemplateProcessorImpl(generator, template);
         }
      }
      throw new IllegalStateException("No generator found for [" + template + "]");
   }

    @Override
    public TemplateProcessor fromTemplate(Resource template) {
        Assert.notNull(template, "Template resource cannot be null");
        Assert.isTrue(template.exists(), "Template does not exist: " + template);
        for (TemplateGenerator generator : generators)
        {
            if (generator.handles(template))
            {
                return new TemplateProcessorImpl(generator, template);
            }
        }
        throw new IllegalStateException("No generator found for [" + template + "]");
    }
}
