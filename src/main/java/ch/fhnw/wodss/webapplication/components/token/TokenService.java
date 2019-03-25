package ch.fhnw.wodss.webapplication.components.token;

import ch.fhnw.wodss.webapplication.components.employee.EmployeeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
public class TokenService {

    private static final String SECRET_KEY = "foobar";

    private static final long VALIDATION_DURATION_IN_MS = 30 * 60 * 1000;

    private final ObjectMapper objectMapper;

    public TokenService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Token createTokenForEmployee(EmployeeDto employee) {
        try {
            Date nowDate = new Date();
            Date expirationDate = new Date(nowDate.getTime() + VALIDATION_DURATION_IN_MS);
            return new Token(Jwts.builder()
                .setIssuer("FHNW wodss")
                .setSubject("Login token")
                .claim("employee", employee)
                .setIssuedAt(nowDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(SECRET_KEY.getBytes()))
                .compact());
        } catch (Exception exception) {
            throw new RuntimeException("Internal token error");
        }
    }

    public Optional<EmployeeDto> getEmployeeFromToken(Token token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(SECRET_KEY.getBytes())).parseClaimsJws(token.getToken()).getBody();
            return Optional.of(objectMapper.convertValue(claims.get("employee"), EmployeeDto.class));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }
}
