package com.projects.vehicle_renting.mapper;

import com.projects.vehicle_renting.dto.RegisterRequest;
import com.projects.vehicle_renting.dto.UserResponse;
import com.projects.vehicle_renting.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(RegisterRequest request);

    UserResponse toUserResponse(User user);
}
