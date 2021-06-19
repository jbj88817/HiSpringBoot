package us.bojie.springdemo.mapper;

import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    void addUser(String userName, String password, String imoocId,String orderId, String createTime);
}
