package site.binghai.biz.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import site.binghai.biz.entity.TokenSeed;
import site.binghai.lib.service.BaseService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TokenSeedService extends BaseService<TokenSeed> {

    @Transactional
    public synchronized List<Long> apply(long size) {
        List<TokenSeed> tokenSeeds = findAll(1);
        TokenSeed tokenSeed = null;
        if (CollectionUtils.isEmpty(tokenSeeds)) {
            init();
        }

        tokenSeed = tokenSeeds.get(0);

        List<Long> res = new ArrayList<>();
        for (long i = 0; i < size; i++) {
            res.add(tokenSeed.getCurrent() + i);
        }
        tokenSeed.setCurrent(tokenSeed.getCurrent() + size);
        save(tokenSeed);

        return res;
    }

    @Transactional
    public TokenSeed init() {
        TokenSeed root = new TokenSeed();
        root.setCurrent(now());
        return save(root);
    }
}
