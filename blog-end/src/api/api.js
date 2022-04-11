import {getRequest,postRequest} from "./request"

/*TUserController.java*/
//用户注册（username+password）
export const _userRegister = (data) =>{
	return postRequest("/user/register",data);
}

//更新用户详细信息
export const _updateUserMoreInfo = (data) =>{
	return postRequest("/user/update/more",data);
}

//更新用户信息
export const _updateUserInfo = (data) =>{
	return postRequest("/user/update",data);
}

//所有角色
export const _getAllRoles = () =>{
	return getRequest("/user/role/all");
}


