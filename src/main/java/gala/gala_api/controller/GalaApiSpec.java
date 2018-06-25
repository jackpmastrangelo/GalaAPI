package gala.gala_api.controller;

import javax.servlet.http.HttpServletResponse;

public class GalaApiSpec {

  public static void setResponseStatusAndMessage(HttpServletResponse response, int statusCode, String message) {
    response.setStatus(statusCode);
    response.setHeader("gala-message", message);
  }
  //TODO is setting gala-message necessary after api-response annotations on controller methods?

}
