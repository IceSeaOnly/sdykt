package site.binghai.biz.controller.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.binghai.biz.entity.Token;
import site.binghai.biz.enums.PrivilegeEnum;
import site.binghai.biz.service.TokenSeedService;
import site.binghai.biz.service.TokenService;
import site.binghai.biz.utils.TokenGenerator;
import site.binghai.lib.controller.BaseController;
import site.binghai.lib.entity.Manager;
import site.binghai.lib.utils.ExcelUtils;
import site.binghai.lib.utils.TimeTools;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/manage/token/")
public class TokenManageController extends BaseController {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private TokenSeedService tokenSeedService;

    @GetMapping("privilegeList")
    public Object privilegeList() {
        JSONArray array = new JSONArray();
        for (PrivilegeEnum p : PrivilegeEnum.values()) {
            JSONObject item = new JSONObject();
            item.put("value", p.name());
            item.put("label", p.getProjectName());

            array.add(item);
        }

        return success(array, null);
    }

    @GetMapping("list")
    public Object list(@RequestParam Integer page) {
        JSONObject obj = new JSONObject();
        page = page < 0 ? 0 : page;
        List<Token> tokens = tokenService.findAll(page, 100);
        obj.put("list", tokens);
        obj.put("total", tokenService.count());
        obj.put("page", page);
        return success(obj, null);
    }

    @GetMapping("download")
    public void download(@RequestParam String ids, HttpServletResponse response) throws IOException {
        List<Long> idList = Arrays.stream(ids.split(","))
            .map(Long::valueOf)
            .collect(Collectors.toList());

        List<Token> tokens = tokenService.findByIds(idList);

        ExcelUtils.builder()
            .mapper("id", "卡序列号")
            .mapper("tokenName", "发卡批次号")
            .mapper("token", "卡号")
            .mapper("secret", "卡密")
            .mapper("privilegeProjectNames", "平台权限")
            .mapper("bindUserId", "绑定ID(如果已激活)")
            .mapper("createdTime", "发卡时间")
            .mapper("activeTimeStr", "激活时间(如果已激活)")
            .putAll(toJSONArray(tokens))
            .webBuild(response, null);
    }

    @GetMapping("delete")
    public Object delete(@RequestParam String ids) {

        Manager manager = getSessionPersistent(Manager.class);

        List<Long> idList = Arrays.stream(ids.split(","))
            .map(Long::valueOf)
            .collect(Collectors.toList());

        tokenService.findByIds(idList)
            .stream()
            .peek(v -> logger.warn("{} deleted Token {}", manager, v))
            .forEach(v -> tokenService.delete(v.getId()));

        String message = String.format("%d 张删除成功!", idList.size());
        return success(message, message);
    }

    @GetMapping("forbidden")
    public Object forbidden(@RequestParam Long tokenId) {
        Manager manager = getSessionPersistent(Manager.class);
        Token token = tokenService.findById(tokenId);
        logger.warn("{} forbidden token {}", manager, token);
        token.setForbidden(Boolean.TRUE);
        tokenService.update(token);

        return success();
    }

    @PostMapping("create")
    public Object create(@RequestBody Map map) {
        String privileges = getString(map, "PRIVILEGES");
        String tokenName = getString(map, "TOKEN_NAME");
        int size = getIntValue(map, "TOKEN_SIZE");

        if (hasEmptyString(privileges)) {
            return fail("PRIVILEGES NOT EXIST");
        }

        privileges = Arrays.asList(privileges.split(","))
            .stream()
            .filter(v -> PrivilegeEnum.valueOfName(v) != null)
            .collect(Collectors.joining(","));

        if (hasEmptyString(privileges)) {
            return fail("PRIVILEGES NOT EXIST");
        }

        List<String> numberList = tokenSeedService.apply(size);
        List<Token> tokens = new ArrayList<>();

        for (String n : numberList) {
            Token token = new Token();
            token.setPrivileges(privileges);
            token.setActiveTime(null);
            token.setTokenName(tokenName);
            token.setToken(n);
            token.setSecret(TokenGenerator.generate(5));

            tokens.add(token);
        }

        tokens = tokenService.batchSave(tokens);

        return success(tokens, null);
    }
}
