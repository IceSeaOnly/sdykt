package site.binghai.biz.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import site.binghai.biz.entity.Token;
import site.binghai.lib.service.BaseService;

import java.util.List;
import java.util.Optional;

@Service
public class TokenService extends BaseService<Token> {
    public Token findByTokenAndSecret(String token_no, String token_secret) {
        Token token = new Token();
        token.setSecret(token_secret);
        token.setToken(token_no);
        return queryOne(token);
    }

    public List<Token> findAllWithoutHiden(Integer page, int pageSize) {
        Token example = new Token();
        example.setHiden(Boolean.FALSE);
        example.setCreated(null);
        example.setCreatedTime(null);
        Example<Token> ex = Example.of(example);
        return getDao().findAll(ex, new PageRequest(page, pageSize)).getContent();
    }
}
