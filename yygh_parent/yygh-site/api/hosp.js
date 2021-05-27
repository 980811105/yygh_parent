import request from '@/utils/request'

const api_name = '/api/hosp/hospital'
export default {
    //查询医院列表
    getPageList(page,limit,searchObj) {
        return request({
            url: `${api_name}/findHospList/${page}/${limit}`,
            method: 'get',
            params: searchObj
        })
    },
    //根据医院名称获取医院列表
    getByHosname(hosname) {
        return request({
            url: `${api_name}/findByHosname/${hosname}`,
            method: 'get'
        })
    },
    //根据医院编号查询医院信息
    show(hoscode) {
        return request({
            url: `${api_name}/findHospDetail/${hoscode}`,
            method: 'get'
        })
    },
    //根据医院编号查询科室信息
    findDepartment(hoscode) {
        return request({
            url: `${api_name}/department/${hoscode}`,
            method: 'get'
        })
    },
    //获取排班规则
    getBookingScheduleRule(page, limit, hoscode, depcode) {
        return request({
            url: `${api_name}/auth/getBookingScheduleRule/${page}/${limit}/${hoscode}/${depcode}`,
            method: 'get'
        })
    },
    //获取排班数据 
    findScheduleList(hoscode, depcode, workDate) {
        return request({
            url: `${api_name}/auth/findScheduleList/${hoscode}/${depcode}/${workDate}`,
            method: 'get'
        })
    },
    //根据id获取排班数据
    getSchedule(scheduleId) {
        return request({
          url: `${api_name}/getSchedule/${scheduleId}`,
          method: 'get'
        })
    }
            
}