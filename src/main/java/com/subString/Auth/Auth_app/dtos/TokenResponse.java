package com.subString.Auth.Auth_app.dtos;

public record TokenResponse(
        String accessToken,
        String refreshToke,
        long expiresIn,
        String tokenType,
        UserDto userdto

) {
   public static TokenResponse  of(String accessToken, String refreshToke, long expiresIn , UserDto userdto) {
        return new TokenResponse(accessToken,refreshToke,expiresIn,"bearer",userdto);
   }
}
