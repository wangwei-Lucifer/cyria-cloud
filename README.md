# cyria-cloud

## Required:
	- 安装 maven
	- 安装 docker-ce
	- 安装 git
	
## 获取源代码:
	- git clone http://<username>@stash.team.kunteng.org.cn/scm/pv/peoplev_backend.git
	
## compile:  
	- mvn package -DskipTests
	- docker-compose -f docker-compose-base.yml -f docker-compose.yml build
	- docker-compose -f docker-compose-base.yml -f docker-compose.yml up

## 注册第一用户
curl -H "Content-Type: application/json" -X POST -d '{"username":"admin","password":"12345678"}' http://localhost:4000/user/register
