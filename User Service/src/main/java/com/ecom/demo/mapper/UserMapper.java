package com.ecom.demo.mapper;

import com.ecom.demo.dto.AddressDTO;
import com.ecom.demo.dto.UserRequest;
import com.ecom.demo.dto.UserResponse;
import com.ecom.demo.entity.Address;
import com.ecom.demo.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse convertToUserResponse(Users user) {
        if(user == null)
            return null;
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId().toString());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setPhoneNumber(user.getPhoneNumber());

        if(user.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipcode(user.getAddress().getZipcode());
            userResponse.setAddress(addressDTO);
        }

        return userResponse;
    }

    public Users convertToUser(UserRequest userRequest) {
        if (userRequest == null)
            return null;

        Users user = new Users();
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPhoneNumber(userRequest.getPhoneNumber());

        if (userRequest.getAddress() != null) {
            Address address = new Address();
            address.setCity(userRequest.getAddress().getCity());
            address.setStreet(userRequest.getAddress().getStreet());
            address.setCountry(userRequest.getAddress().getCountry());
            address.setZipcode(userRequest.getAddress().getZipcode());

            user.setAddress(address);
        }

        return user;
    }


}
