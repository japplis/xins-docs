package com.mycompany.cc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xins.common.MandatoryArgumentChecker;
import org.xins.common.text.ParseException;
import org.xins.common.xml.Element;
import org.xins.server.CustomCallingConvention;
import org.xins.server.FunctionNotSpecifiedException;
import org.xins.server.FunctionRequest;
import org.xins.server.FunctionResult;
import org.xins.server.InvalidRequestException;

public class MyCC extends CustomCallingConvention {

   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Creates a new <code>MyCC</code> instance.
    */
   public MyCC() {
      // empty
   }


   //-------------------------------------------------------------------------
   // Methods
   //-------------------------------------------------------------------------

   /**
    * Checks if the specified request can be handled by this calling 
    * convention.
    *
    * @param httpRequest
    *    the HTTP request to investigate, never <code>null</code>.
    *
    * @return
    *    <code>true</code> if this calling convention is <em>possibly</em>
    *    able to handle this request, or <code>false</code> if it
    *    <em>definitely</em> not able to handle this request.
    */
   protected boolean matches(HttpServletRequest httpRequest) {

      // Get all values for the "_function" parameter
      String[] values = httpRequest.getParameterValues("_function");

      // There should be exactly one value
      if (values == null || values.length != 1) {
         return false;
      }

      // Only the performance statistics function is supported
      if (! "_GetStatistics".equals(values[0])) {
         return false;
      }

      // All requirements are met; there is a match
      return true;
   }

   /**
    * Converts an HTTP request to a XINS request (implementation method).
    *
    * This method is invoked from the XINS framework when ahn HTTP request
    * comes in.
    *
    * @param httpRequest
    *    the HTTP request to convert, will not be <code>null</code>.
    *
    * @return
    *    the XINS request object, should not be <code>null</code>.
    *
    * @throws InvalidRequestException
    *    if the request is considerd to be invalid.
    *
    * @throws FunctionNotSpecifiedException
    *    if the request does not indicate the name of the function to execute.
    */
   protected FunctionRequest convertRequestImpl(HttpServletRequest httpRequest)
   throws InvalidRequestException, FunctionNotSpecifiedException {

      // Get all values for the "_function" parameter
      String[] values = httpRequest.getParameterValues("_function");

      // The function name must be specified
      if (values == null || values.length == 0) {
         throw new FunctionNotSpecifiedException();
      }

      // There can only be one value for the "_function" parameter
      if (values.length > 1) {
         throw new InvalidRequestException("Multiple values specified for parameter \"_function\".");
      }

      // Only the performance statistics function is supported
      if (! "_GetStatistics".equals(values[0])) {
         throw new InvalidRequestException("Only function \"_GetStatistics\" is supported by this calling convention.");
      }

      // Return an appropriate XINS request object
      return new FunctionRequest("_GetStatistics", null, null);
   }

   /**
    * Converts a XINS result to an HTTP response (implementation method).
    *
    * @param xinsResult
    *    the XINS result object that should be converted to an HTTP response,
    *    will not be <code>null</code>.
    *
    * @param httpResponse
    *    the HTTP response object to configure, will not be <code>null</code>.
    *
    * @param httpRequest
    *    the HTTP request, will not be <code>null</code>. 
    *
    * @throws IOException
    *    if calling any of the methods in <code>httpResponse</code> causes an
    *    I/O error.
    */
   protected void convertResultImpl(FunctionResult      xinsResult,
                                    HttpServletResponse httpResponse,
                                    HttpServletRequest  httpRequest)
   throws IOException {

      // The result is always an HTTP OK (code 200)
      httpResponse.setStatus(HttpServletResponse.SC_OK);

      // Content type is always plain text, character encoding UTF-8
      httpResponse.setContentType("text/plain;charset=UTF-8");
      // TODO httpResponse.setContentType("text/csv;charset=UTF-8");

      // Output the headers
      PrintWriter writer = httpResponse.getWriter();
      writer.println("Function,SuccessCount,SuccessAvg,ErrorCount,ErrorAvg");

      // Get the data element
      Element data = xinsResult.getDataElement();
      if (data != null) {

         // Loop over all contained <function> elements
         List functions = data.getChildElements("function");
         int functionCount = (functions == null) ? 0 : functions.size();
         for (int i = 0; i < functionCount; i++) {

            // Determine the function name
            Element function = (Element) functions.get(i);
            String functionName = function.getAttribute("name");

            // Determine the success statistics for this function
            Element success = getChild(function, "successful");
            String successCount = success.getAttribute("count");
            String successAvg   = success.getAttribute("average");

            // Determine the error statistics for this function
            Element error = getChild(function, "unsuccessful");
            String errorCount = error.getAttribute("count");
            String errorAvg   = error.getAttribute("average");

            // Write the line with comma-separated values
            writer.println(functionName + ","
                         + successCount + ","
                         + successAvg   + ","
                         + errorCount   + ","
                         + errorAvg);
         }
      }

      // Flush the output to the client
      writer.flush();
   }

   /**
    * Gets a single child element from the specified element. The element must
    * exist, otherwise a <code>RuntimeException</code> is thrown.
    *
    * @param element
    *    the element to get a child element from, should not be 
    *    <code>null</code>.
    *
    * @param name
    *    the name of the child element, should not be <code>null</code>.
    *
    * @throws RuntimeException
    *    if the specified element does not have exactly one child with the 
    *    specified name.
    */
   private final Element getChild(Element element, String name) {
      try {
         return element.getUniqueChildElement(name);
      } catch (ParseException exception) {
         String message = "Expected element \""
                        + element
                        + "\" to have exactly one child element named \""
                        + name
                        + "\".";
         throw new RuntimeException(message);
      }
   }
}
