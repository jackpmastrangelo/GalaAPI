package gala.gala_api.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

class GalaApiSpec {

  static void sendError(HttpServletResponse response, int statusCode, String message) {
    try {
      response.sendError(statusCode, message);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
