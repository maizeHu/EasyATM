package maizehu;

import java.util.Scanner;

public class ATM {
    private Scanner rs = new Scanner(System.in);
    private Account loninAcc; // 记住登录后的账户

    public void start() {
        while (true) {
            System.out.println("===欢迎进入到ATM系统===");
            System.out.println("1.用户登录");
            System.out.println("2.用户开户");
            System.out.println("请选择");
            int command = rs.nextInt();
            switch (command) {
                case 1:
                    // 用户登录
                    login();
                    break;
                case 2:
                    createAccount();
                    // 用户开户
                    break;
                default:
                    System.out.println("没有该操作");
            }
        }
    }

    // 完成用户的登录操作
    private void login() {
        System.out.println("==系统登录==");
        while (true) {
            // 让用户输入卡号
            System.out.println("请输入你的卡号");
            String carfId = rs.next();

            // 从数据库中查询账户信息
            Account acc = Account.getAccountByCardId(carfId);
            if (acc == null) {
                System.out.println("你输入的卡号不存在，请重试");
            } else {
                // 卡号存在了，接着让用户输入密码
                System.out.println("请输入密码");
                String passWord = rs.next();
                // 判断密码是否正确
                if (acc.getPassWord().equals(passWord)) {
                    loninAcc = acc;
                    // 密码正确，登录成功
                    System.out.println("恭喜你，" + acc.getUesrname() + "登录成功，您的卡号是" + acc.getCardid());
                    // 展示登录后的操作界面
                    showUserCommand();
                    return; // 跳出并结束登录方法
                } else {
                    System.out.println("你输入的密码不正确，请重试");
                }
            }
        }
    }

    // 展示登陆后的操作界面
    private void showUserCommand() {
        while (true) {
            System.out.println(loninAcc.getUesrname() + "您可以选择如下功能进行账户的处理====");
            System.out.println("1.账户查询");
            System.out.println("2.存款");
            System.out.println("3.取款");
            System.out.println("4.转账");
            System.out.println("5.密码修改");
            System.out.println("6.退出");
            System.out.println("7.注销当前账户");
            int command = rs.nextInt();
            switch (command) {
                case 1:
                    // 查询当前账户
                    showLoginAccount();
                    break;
                case 2:
                    // 存款
                    depositMoney();
                    break;
                case 3:
                    drawMonry();
                    // 取款
                    break;
                case 4:
                    transforMonry();
                    // 转账
                    break;
                case 5:
                    // 密码修改
                    updatePassword();
                    return;
                case 6:
                    System.out.println(loninAcc.getUesrname() + "退出系统成功");
                    // 退出
                    return; // 跳出结束当前方法
                case 7:
                    // 注销当前账户
                    if (deleteAccount()) {
                        // 销户了，回到欢迎页面
                        return;
                    }
                    break;
                default:
                    System.out.println("当前的操作是不存在的，请重新确认操作");
            }
        }
    }

    // 密码修改
    private void updatePassword() {
        System.out.println("==账户密码修改工作===");
        while (true) {
            System.out.println("请输入当前账户的密码");
            String passWord = rs.next();
            // 认证当前密码是否正确
            if (loninAcc.getPassWord().equals(passWord)) {
                // 修改密码
                while (true) {
                    System.out.println("请输入新密码");
                    String newPassWord = rs.next();
                    System.out.println("确认密码");
                    String okPassWord = rs.next();
                    if (okPassWord.equals(newPassWord)) {
                        loninAcc.setPassWord(newPassWord);
                        loninAcc.updateInDatabase();
                        System.out.println("恭喜你，密码修改成功！");
                        return;
                    } else {
                        System.out.println("两次输入的密码不一致");
                    }
                }
            } else {
                System.out.println("输入的密码不正确，请重新输入");
            }
        }
    }

    // 销户
    private boolean deleteAccount() {
        System.out.println("==进行销户操作==");
        // 询问用户是否真的销户
        System.out.println("请问你是真的销户吗 y/n ?");
        String command = rs.next();
        switch (command) {
            case "y":
                // 确实要销
                // 判断用户的账户中是否有钱
                if (loninAcc.getMoney() == 0) {
                    loninAcc.deleteFromDatabase();
                    System.out.println("恭喜你，销户成功");
                    return true;
                } else {
                    System.out.println("您的账户里还有钱，不能销户");
                    return false;
                }
            default:
                System.out.println("好的，您的账户保留");
                return false;
        }
    }

    // 转账
    private void transforMonry() {
        System.out.println("==用户转账==");
        // 判断自己账户是否有钱
        if (loninAcc.getMoney() == 0) {
            System.out.println("账户余额为空，无法进行转账操作");
            return;
        }

        while (true) {
            // 进行转账
            System.out.println("请输入对方的卡号:");
            String cardId = rs.next();

            // 判断这个卡号是否正确
            Account acc = Account.getAccountByCardId(cardId);
            if (acc == null) {
                System.out.println("您输入的卡号有问题，请重试");
            } else {
                // 对方账户存在
                String name = '*' + acc.getUesrname().substring(1);
                System.out.println("请您输入【 " + name + "】的姓氏");
                String preName = rs.next();
                // 判断这个姓氏是否正确
                if (acc.getUesrname().startsWith(preName)) {
                    while (true) {
                        // 认证通过
                        System.out.println("请输入给对方转账的金额");
                        double money = rs.nextDouble();
                        // 判断自己账户的余额是否足够
                        if (loninAcc.getMoney() >= money) {
                            // 更新自己的账户余额
                            loninAcc.setMoney(loninAcc.getMoney() - money);
                            loninAcc.updateInDatabase();
                            // 更新对方账户的余额
                            acc.setMoney(acc.getMoney() + money);
                            acc.updateInDatabase();
                            System.out.println("恭喜你，转账成功");
                            return; // 跳出转账方法
                        } else {
                            System.out.println("您的余额不足不能转这么多，当前余额为：" + loninAcc.getMoney());
                        }
                    }
                } else {
                    System.out.println("对不起，您认证的姓氏有误");
                }
            }
        }
    }

    // 取钱
    private void drawMonry() {
        System.out.println("==取钱操作==");
        // 1. 判断账户余额是否达到了10元，如果不到就不能取钱
        if (loninAcc.getMoney() < 10) {
            System.out.println("您的账户余额不足10元，不允许取钱");
            return;
        }
        // 让用户输入取款金额
        while (true) {
            System.out.println("请输入您要取的金额");
            double money = rs.nextDouble();

            // 判断账户余额是否足够
            if (loninAcc.getMoney() >= money) {
                // 账户余额足够多时，判断是否超过了单次限额
                if (loninAcc.getLimit() < money) {
                    System.out.println("您当前取款金额超过了当前单次限额，每次最多可取：" + loninAcc.getLimit());
                } else {
                    loninAcc.setMoney(loninAcc.getMoney() - money);
                    loninAcc.updateInDatabase();
                    System.out.println("您取款：" + money + "成功，取款后余额：" + loninAcc.getMoney());
                    break;
                }
            } else {
                System.out.println("余额不足，您的当前账户的余额是：" + loninAcc.getMoney());
            }
        }
    }

    // 存钱
    private void depositMoney() {
        System.out.println("==存钱操作==");
        System.out.println("请输入存款金额");
        double money = rs.nextDouble();
        // 更新当前登录的账户的余额
        loninAcc.setMoney(loninAcc.getMoney() + money);
        loninAcc.updateInDatabase();
        System.out.println("恭喜你，您存钱：" + money + "成功，存钱后余额为" + loninAcc.getMoney());
    }

    // 展示当前账户的信息
    private void showLoginAccount() {
        System.out.println("==当前账户信息如下==");
        System.out.println("卡号" + loninAcc.getCardid());
        System.out.println("户主" + loninAcc.getUesrname());
        System.out.println("性别" + loninAcc.getSex());
        System.out.println("余额" + loninAcc.getMoney());
        System.out.println("每次取款限额" + loninAcc.getLimit());
        System.out.println("请输入：");
    }

    // 开户操作
    private void createAccount() {
        // 1. 创建一个账户对象
        Account acc = new Account();
        // 2. 需要用户输入自己的开户信息，赋值给账户对象
        System.out.println("请输入你的账户名称");
        String name = rs.next();
        acc.setUesrname(name);

        while (true) {
            System.out.println("输入你的性别");
            char sex = rs.next().charAt(0);
            if (sex == '男' || sex == '女') {
                acc.setSex(sex);
                break;
            } else {
                System.out.println("输入的性别只能是男或者女");
            }
        }
        while (true) {
            System.out.println("输入你的账户密码");
            String passWord = rs.next();
            System.out.println("确认密码");
            String okpassWord = rs.next();
            // 判断两次密码是否一致
            if (okpassWord.equals(passWord)) {
                acc.setPassWord(okpassWord);
                break;
            } else {
                System.out.println("两次的密码不一致，请再次确认");
            }
        }

        System.out.println("请输入你的取现额度");
        double limit = rs.nextDouble();
        acc.setLimit(limit);

        // 重点；我们需要为这个账户生成一个卡号（由系统自动生成，8位数字表示，不能与其他账户的卡号重复）
        String newCardId = createCardId();
        acc.setCardid(newCardId);

        // 3. 把这个账户对象存入到数据库里去
        acc.saveToDatabase();
        System.out.println("恭喜你，" + acc.getUesrname() + "开户成功，您的卡号是" + acc.getCardid());
    }

    // 返回8位数字表示，不能与其他账户的卡号重复
    private String createCardId() {
        while (true) {
            // 1. 定义一个String类型的变量记住八位数字
            String cardId = "";
            // 2. 使用循环，循环8次，每一次产生一个随机数给cardId连接起来
            java.util.Random r = new java.util.Random();
            for (int i = 0; i < 8; i++) {
                int data = r.nextInt(10);
                cardId += data;
            }
            // 3. 判断cardId中记住的卡号，是否与其他账户的卡号重复了，没有重复，才可以作为一个新的卡号返回
            Account acc = Account.getAccountByCardId(cardId);
            if (acc == null) {
                // 说明cardId没有找到账户对象，因此cardId没有与其他账户的卡号重复，可以返回它作为一个新卡号
                return cardId;
            }
        }
    }
}