package org.jboss.forge.addon.text.highlight.scanner;

import static org.jboss.forge.addon.text.highlight.encoder.AssertEncoder.assertTextToken;

import org.jboss.forge.addon.text.highlight.Syntax;
import org.jboss.forge.addon.text.highlight.Syntax.Builder;
import org.jboss.forge.addon.text.highlight.TokenType;
import org.junit.Ignore;
import org.junit.Test;

public class CSSScannerTestCase extends AbstractScannerTestCase
{

   @Test
   @Ignore
   // simple developer test
   public void should() throws Exception
   {

      String source = "/* See http://reference.sitepoint.com/css/content. */\n" +
               "@media print {\n" +
               "  a[href]:after {\n" +
               "    content: \"<\" attr(href) \">\";\n" +
               "  }\n" +
               "}\n" +
               "\n" +
               "a:link:after, a:visited:after {content:\" (\" attr(href) \")\";font-size:90%;}\n" +
               "ol {\n" +
               "  counter-reset: item;\n" +
               "  margin: 0;\n" +
               "  padding: 0.7px;\n" +
               "}\n" +
               ".some {}" +
               "ol>li {\n" +
               "  counter-increment: item;\n" +
               "  list-style: none inside;\n" +
               "}\n" +
               "ol>li:before {\n" +
               "  content: counters(item, \".\") \" - \";\n" +
               "}\n" +
               "\n" +
               "body {\n" +
               "  counter-reset: chapter;\n" +
               "}\n" +
               "h1 {\n" +
               "  counter-increment: chapter;\n" +
               "  counter-reset: section;\n" +
               "}\n" +
               "h2 {\n" +
               "  counter-increment: section;\n" +
               "}\n" +
               "h2:before {\n" +
               "  content: counter(chapter) \".\" counter(section) \" \";\n" +
               "}\n";

      Syntax.Builder.create().scannerType(CSSScanner.TYPE.getName()).encoderType(ASSERT_ENCODER).execute(source);

      assertTextToken(TokenType.attribute_name, "href");
      assertTextToken(TokenType.directive, "@media");
      assertTextToken(TokenType.comment, "/* See http://reference.sitepoint.com/css/content. */");
      assertTextToken(TokenType.tag, "a", "body", "ol");
      assertTextToken(TokenType.class_, ".some");
      assertTextToken(TokenType.float_, "0", "0.7px");
      assertTextToken(TokenType.key, "list-style", "counter-increment", "margin");
      assertTextToken(TokenType.operator, ";", "{", "}", ",");
   }

   @Test
   public void shoulMatchCssStandardExample() throws Exception
   {
      assertMatchExample(Builder.create(), "css", "standard.in.css");
   }

   @Test
   @Ignore
   // Some new line issue
   public void shoulMatchCssYUIExample() throws Exception
   {
      assertMatchExample(Builder.create(), "css", "yui.in.css");
   }

   @Test
   public void shoulMatchCssDemoExample() throws Exception
   {
      assertMatchExample(Builder.create(), "css", "demo.in.css");
   }

   @Test
   public void shoulMatchCssCoderayExample() throws Exception
   {
      assertMatchExample(Builder.create(), "css", "coderay.in.css");
   }

   @Test
   public void shoulMatchCssRadmineExample() throws Exception
   {
      assertMatchExample(Builder.create(), "css", "redmine.in.css");
   }

   @Test
   @Ignore
   // Some issue hidden char in first pos?
   public void shoulMatchCssIgnosDraconisExample() throws Exception
   {
      assertMatchExample(Builder.create(), "css", "ignos-draconis.in.css");
   }

   @Test
   @Ignore
   // Some issue with new_line in output, revisit
   public void shoulMatchCssS5Example() throws Exception
   {
      assertMatchExample(Builder.create(), "css", "S5.in.css");
   }
}
