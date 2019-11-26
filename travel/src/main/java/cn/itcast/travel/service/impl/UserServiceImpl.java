package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();
    /**
     * 注册用户
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
        //1.查询用户名
        User u = userDao.findByUsername(user.getUsername());
        //判断u是否为空
        if (u != null){
            //用户名存在，注册失败
            return false;
        }
        //2.保存用户信息
        user.setCode(UuidUtil.getUuid());//设置激活码，唯一
        user.setStatus("N");//设置激活状态，N为未激活，Y为激活
        userDao.save(user);
        //3.激活邮件发送，邮件正文
        String context = "<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>点击激活【黑马旅游网】</a>";
        MailUtils.sendMail(user.getEmail(),context,"激活邮件");
        return true;
    }

    @Override
    public boolean active(String code) {
        //根据激活码查询用户对象
        User user = userDao.findByCode(code);
        //判断激活码是否为空
        if (user != null){
            userDao.updateStatus(user);
            return true;
        }else{
            return false;
        }

    }

    /**
     * 用户登陆
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
       return userDao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
}
