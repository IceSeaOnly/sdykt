package site.binghai.biz.service;

import org.springframework.stereotype.Service;
import site.binghai.biz.entity.Token;
import site.binghai.lib.service.BaseService;

@Service
public class TokenService extends BaseService<Token> {
    public Token findByTokenAndSecret(String token_no, String token_secret) {
        Token token = new Token();
        token.setSecret(token_secret);
        token.setToken(token_no);
        return queryOne(token);
    }
}
