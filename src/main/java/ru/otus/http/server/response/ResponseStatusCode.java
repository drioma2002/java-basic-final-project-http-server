package ru.otus.http.server.response;

public enum ResponseStatusCode {
    CODE_200_OK(200, "OK"),
    CODE_201_Created(201, "Created"),
    CODE_204_NoContent(204, "No Content"),
    CODE_400_BadRequest(400, "Bad Request"),
    CODE_404_NotFound(404, "Not Found"),
    CODE_405_MethodNotAllowed(405, "Method not allowed"),
    CODE_422_UnprocessableEntity(422, "Unprocessable Entity"),
    CODE_500_InternalServerError(500, "Internal Server Error");

    public final int STATUS_CODE;
    public final String DESCRIPTION;

    ResponseStatusCode(int STATUS_CODE, String DESCRIPTION) {
        this.STATUS_CODE = STATUS_CODE;
        this.DESCRIPTION = DESCRIPTION;
    }
}
