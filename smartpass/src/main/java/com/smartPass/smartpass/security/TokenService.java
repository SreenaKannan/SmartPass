package com.smartPass.smartpass.security;
import com.smartPass.smartpass.entity.User; import org.springframework.stereotype.Service;
import java.util.*; import java.util.concurrent.ConcurrentHashMap;
@Service public class TokenService {
  private final Map<String, User> tokens = new ConcurrentHashMap<>();
  public String issue(User user) { String token=UUID.randomUUID().toString()+UUID.randomUUID(); tokens.put(token,user); return token; }
  public User find(String token) { return tokens.get(token); }
}
