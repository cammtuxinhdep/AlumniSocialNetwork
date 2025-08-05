/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.utils;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.util.Date;

/**
 *
 * @author HP
 */
public class JwtUtils {
    // SECRET nên được lưu bằng biến môi trường,
    private static final String SECRET = "12345678901234567890123456789012"; // 32 ký tự (AES key)
    private static final long EXPIRATION_MS = 86400000; // 1 ngày

    // Tạo một JWT cho người dùng dựa trên username
    public static String generateToken(String username) throws Exception {
        JWSSigner signer = new MACSigner(SECRET);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .expirationTime(new Date(System.currentTimeMillis() + EXPIRATION_MS)) // Ngày hết hạn = hiện tại + 1
                .issueTime(new Date()) // Thời gian cấp token
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claimsSet
        );

        signedJWT.sign(signer); // Kí token bằng khóa bí mật

        return signedJWT.serialize();
    }

    // Xác thực token và lấy username từ token nếu hợp lệ
    public static String validateTokenAndGetUsername(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token); // Phân tích token lấy đối tượng signedJWT
        JWSVerifier verifier = new MACVerifier(SECRET); // Tạo verifier với khóa bí mật => kiểm tra chữ kí

        if (signedJWT.verify(verifier)) {
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expiration.after(new Date())) {
                return signedJWT.getJWTClaimsSet().getSubject(); // Token hợp lệ chưa hết hạn trả về username
            }
        }
        return null;
    }
}