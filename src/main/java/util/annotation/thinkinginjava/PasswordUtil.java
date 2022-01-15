package util.annotation.thinkinginjava;

import java.util.List;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/1/15 16:46
 * @version:1.0
 */
public class PasswordUtil {
    @UseCase(id = 1)
    public boolean checkForNewPassword(List<String> prevPasswords, String password) {
        return !prevPasswords.contains(password);
    }
}
