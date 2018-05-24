### 版本控制神器git的使用

- 核心：分布式，快照，状态区，分支

- 安装：brew cask install git

- git --version 查看版本

- git config --list 查看当前git的配置信息

- git config --list --global 查看所有git项目的通用配置信息

- git config user.name 查看配置信息的指定属性

- git config --global user.name ljy 设置配置信息的指定属性

- git config --global --add user.name ljy user.email xxx@yy.com 设置配置信息的多个属性

- git config --global --unset user.name ljy 删除配置信息的指定属性

- git config --global alias.lg  "log --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr)%Creset' --abbrev-commit --date=relative"
    设置别名

- git init  将一个目录快速设置成git的代码仓库，成功后该目录下会有.git隐藏文件夹
        
- git add abc.txt 将代码提交到暂存区
    
- git commit -m "add abc" 提交修改

- git commit -amend -m "add abc.txt" 追加修改，上次commit有错时进行更正

- git status  查看代码仓库的变化

- git diff abc.txt 查看指定文件的具体变化

- git diff HEAD/HEAD^/HEAD^^/HEAD~3  比较提交节点间的差异

- git log 查看提交记录  
  
- git blame README.md 查看指定文件的历史修改记录  

- git checkout README.md 修改文件后，执行git add前，如果执行checkout就会抛弃当前所有修改;
    如果是执行git add后再执行checkout就会恢复到add时的初始状态    

- git reset --hard HEAD/HEAD^/HEAD^^/HEAD~3 回退到指定的版本 
 
- git reflog 查看所有的提交记录，包括被reset的记录以及reset记录

- git rm test.txt 删除git仓库中文件,需要commit

- git remote add origin https://github.com/JinYangLiu/testGit.git \
  git push -u origin master
  将本地既存的repo提交到远程仓库如github的repo中，其实在github创建新仓库的成功页面会有初始化仓库及提交到github的具体提示
 
- git push 与github等远程仓库关联后，在修改文件后，不仅需要之前的add，commit操作，最后还需要执行push，将修改提交到远程仓库
 
- git pull 用以拉取远程仓库的最新代码，更新本地仓库 

- git clone https://github.com/JinYangLiu/Kotlin.git 将远程仓库的项目克隆到本地

- git checkout -b dev  创建一个新的分支dev并切换到新的分支上(相当于下面两条)

- git branch dev 创建新的分支dev

- git checkout dev 切换到指定分支dev

- git branch 查看当前所有本地分支

- git merge dev 合并分支dev到当前分支（保持了完整的提交记录）

- git rebase dev 也是合并分支，区别于merge的是git的时间线会被合并（时间线更加干净）

- git branch -d dev 删除分支dev

-（与svn不同的是，git无论创建分支还是记录版本，都不是创建整个文件或分支的备份，而是创建一个指针，指向不同的文件或分支，
切换分支或版本都是改变指针的位置，实际git只用很少的存储空间记录这一切）

-（实际开发中，建议主分支master用以进行发布管理，应保证主分支的安全；新建一个分支如dev，供开发者进行checkout出dev分支进行开发，
及merge自己的分支到dev分支，在发布版本前再将dev合并到主分支）

- git remote -v 查看远程分支（-v 表示查看详细信息）

- git push origin dev 将本地创建的分支同步到远程仓库上

- git checkout -b dev_ljy origin/dev 上一步提交了本地分支到远程仓库，然而再次克隆远程仓库或者pull时时找不到dev分支的，
    需要执行checkout ，将远程分支checkout到本地（相当于本地仓库新建一个分支）
 
- git ls-remote 显示远程仓库分支的完整列表（可以用来检验上上步的push是否成功）

- git remote show origin 查看远程分支的更多信息 

- git push origin dev_ljy:dev 提交对远程分支的修改，需要commit后使用，git push <远程仓库名> <本地分支名>:<远程分支名>

- git tag version_1  /git tag version_1 1322663  添加tag，version_1是tag名，可以自己随意定义 ，1322663是通过commit id来指定要添加tag的地方

- git tag -a version_1 -m "我是tag version_1 的注释文字"  添加tag，并带注释

- git tag 用来查看已添加的tag

- git tag -d version_1 删除指定的tag

-  git show version_1 查看指定tag的详细信息

- git push origin version_1 推送指定tag到远程仓库

- git push origin --tags 提交所有tag



