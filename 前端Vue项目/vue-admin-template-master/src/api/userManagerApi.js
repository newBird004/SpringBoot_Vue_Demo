import request from '@/utils/request'

export function getUserList(queryParam) {
  return request({
    url: '/user/list',
    method: 'get',
    params: {
      pageNo: queryParam.pageNo,
      pageSize: queryParam.pageSize,
      phone: queryParam.phone,
      username: queryParam.username,
    }
  })
}


export function addUser(user) {
  return request({
    url: '/user/add',
    method: 'post',
    data: user
  })
}


export function getUserById(id) {
  return request({
    url: '/user/' + id,
    method: 'get',
    data: id
  })
}

export function updateUser(user) {
  return request({
    url: '/user',
    method: 'put',
    data: user
  })
}

export function removeByUserId(id) {
  return request({
    url: '/user/' + id,
    method: 'delete',
    data: id
  })
}

