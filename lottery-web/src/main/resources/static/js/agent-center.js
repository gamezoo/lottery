var agentCenterVM = new Vue({
	el : '#agent-center',
	data : {
		currentTab : '',
		rebateAndOddses : [],

		/**
		 * 下级账号管理相关参数start
		 */
		userNameLowerLevelAccountManage : '',
		queryScopeLowerLevelAccountManage : '',
		queryScopeDictItems : [ {
			dictItemCode : '20',
			dictItemName : '指定账号及直接下级'
		}, {
			dictItemCode : '10',
			dictItemName : '所有账号'
		} ],

		/**
		 * 代理开户相关参数start
		 */
		userNameAgentOpenAnAccount : '',
		loginPwdAgentOpenAnAccount : '',
		confirmLoginPwdAgentOpenAnAccount : '',
		rebateAndOddsAgentOpenAnAccount : '',

		/**
		 * 下级开户相关参数start
		 */
		accountTypeLowerLevelOpenAnAccount : '',
		rebateAndOddsLowerLevelOpenAnAccount : '',
		inviteRegisterLink : null,

	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.initRebateAndOddsData();
		this.switchLowerLevelAccountManageTab();

		var clipboard = new ClipboardJS('#copyInviteRegisterLinkBtn');
		clipboard.on('success', function(e) {
			layer.alert('复制成功!', {
				icon : 1,
				time : 3000,
				shade : false
			});
		});
	},
	methods : {
		initRebateAndOddsData : function() {
			var that = this;
			this.$http.get('/agent/findAllRebateAndOdds').then(function(res) {
				var resResult = res.body.data;
				var currentAccountRebateAndOddsAddedFlag = false;
				var rebateAndOddses = [];
				for (var i = 0; i < resResult.length; i++) {
					if (resResult[i].rebate <= headerVM.rebate) {
						rebateAndOddses.push({
							rebate : resResult[i].rebate,
							odds : resResult[i].odds
						});
						if (resResult[i].rebate == headerVM.rebate) {
							currentAccountRebateAndOddsAddedFlag = true;
						}
					}
				}
				if (!currentAccountRebateAndOddsAddedFlag) {
					rebateAndOddses.push({
						rebate : headerVM.rebate,
						odds : headerVM.odds
					});
				}
				that.rebateAndOddses = rebateAndOddses;
			});
		},

		/**
		 * 下级账号管理tab相关方法start
		 */
		switchLowerLevelAccountManageTab : function() {
			this.currentTab = 'lowerLevelAccountManage';
			this.resetLowerLevelAccountManageQueryCond();
			this.initLowerLevelAccountManageTable();
		},

		resetLowerLevelAccountManageQueryCond : function() {
			this.userNameLowerLevelAccountManage = headerVM.userName;
			this.queryScopeLowerLevelAccountManage = '20';
		},

		initLowerLevelAccountManageTable : function() {
			var that = this;
			$('.lower-level-account-manage-table').bootstrapTable('destroy');
			$('.lower-level-account-manage-table').bootstrapTable({
				classes : 'table table-hover',
				height : 540,
				url : '/agent/findLowerLevelAccountDetailsInfoByPage',
				pagination : true,
				sidePagination : 'server',
				pageNumber : 1,
				pageSize : 10,
				pageList : [ 10, 25, 50, 100 ],
				queryParamsType : '',
				queryParams : function(params) {
					var condParam = {
						pageSize : params.pageSize,
						pageNum : params.pageNumber,
						userName : that.userNameLowerLevelAccountManage,
						queryScope : that.queryScopeLowerLevelAccountManage
					};
					return condParam;
				},
				responseHandler : function(res) {
					if (res.code != 200) {
						layer.alert(res.msg, {
							title : '提示',
							icon : 7,
							time : 3000
						});
						return {
							total : 0,
							rows : []
						};
					}
					return {
						total : res.data.total,
						rows : res.data.content
					};
				},
				columns : [ {
					title : '用户名',
					formatter : function(value, row, index) {
						var userName = row.userName;
						if (row.accountType == 'admin') {
							return userName + '(' + row.accountTypeName + ')';
						}
						return userName + '(' + row.accountLevel + '级' + row.accountTypeName + ')';
					},
					cellStyle : {
						classes : 'user-name'
					}
				}, {
					title : '返点/赔率',
					formatter : function(value, row, index) {
						if (row.rebate == null) {
							return;
						}
						return row.rebate + '%/' + row.odds;
					}
				}, {
					field : 'balance',
					title : '余额'
				}, {
					field : 'registeredTime',
					title : '注册时间'
				}, {
					field : 'latelyLoginTime',
					title : '最近登录时间',
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						// return [ '<button type="button"
						// class="account-edit-btn btn btn-outline-primary
						// btn-sm" style="margin-right: 4px;">编辑</button>',
						// '<button type="button" class="modify-login-pwd-btn
						// btn btn-outline-secondary btn-sm"
						// style="margin-right: 4px;">修改登录密码</button>', '<button
						// type="button" class="del-account-btn btn
						// btn-outline-danger btn-sm">删除</button>' ].join('');
					},
					events : {
						'click .account-edit-btn' : function(event, value, row, index) {
							that.showAccountEditModal(row);
						}
					}
				} ]
			});
		},

		refreshLowerLevelAccountManageTable : function() {
			$('.lower-level-account-manage-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		/**
		 * 代理开户tab相关方法start
		 */
		switchAgentOpenAnAccountTab : function() {
			this.currentTab = 'agentOpenAnAccount';
			this.userNameAgentOpenAnAccount = '';
			this.loginPwdAgentOpenAnAccount = '';
			this.confirmLoginPwdAgentOpenAnAccount = '';
			this.rebateAndOddsAgentOpenAnAccount = '';
		},

		agentOpenAnAccount : function() {
			var that = this;
			if (that.userNameAgentOpenAnAccount == null || that.userNameAgentOpenAnAccount == '') {
				layer.alert('请输入用户名', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var userNamePatt = /^[A-Za-z][A-Za-z0-9]{5,11}$/;
			if (!userNamePatt.test(that.userNameAgentOpenAnAccount)) {
				layer.alert('用户名不合法!请输入以字母开头,长度为6-12个字母和数字的用户名');
				return;
			}
			if (that.loginPwdAgentOpenAnAccount == null || that.loginPwdAgentOpenAnAccount == '') {
				layer.alert('请输入登录密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.confirmLoginPwdAgentOpenAnAccount == null || that.confirmLoginPwdAgentOpenAnAccount == '') {
				layer.alert('请确认登录密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.loginPwdAgentOpenAnAccount != that.confirmLoginPwdAgentOpenAnAccount) {
				layer.alert('密码不一致');
				return;
			}
			var passwordPatt = /^[A-Za-z][A-Za-z0-9]{5,14}$/;
			if (!passwordPatt.test(that.loginPwdAgentOpenAnAccount)) {
				layer.alert('密码不合法!请输入以字母开头,长度为6-15个字母和数字的密码');
				return;
			}
			if (that.rebateAndOddsAgentOpenAnAccount == null || that.rebateAndOddsAgentOpenAnAccount == '') {
				layer.alert('请选择返点/赔率', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/agent/agentOpenAnAccount', {
				userName : that.userNameAgentOpenAnAccount,
				loginPwd : that.loginPwdAgentOpenAnAccount,
				rebate : that.rebateAndOddsAgentOpenAnAccount.rebate,
				odds : that.rebateAndOddsAgentOpenAnAccount.odds
			}).then(function(res) {
				layer.alert('开户成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.switchAgentOpenAnAccountTab();
			});
		},

		/**
		 * 下级开户tab相关方法start
		 */
		switchLowerLevelOpenAnAccountTab : function() {
			this.currentTab = 'lowerLevelOpenAnAccount';
			this.accountTypeLowerLevelOpenAnAccount = 'member';
			this.rebateAndOddsLowerLevelOpenAnAccount = '';
			this.inviteRegisterLink = null;
		},

		generateRegiterLink : function() {
			var that = this;
			if (that.accountTypeLowerLevelOpenAnAccount == null || that.accountTypeLowerLevelOpenAnAccount == '') {
				layer.alert('请选择开户类型', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.rebateAndOddsLowerLevelOpenAnAccount == null || that.rebateAndOddsLowerLevelOpenAnAccount == '') {
				layer.alert('请选择返点/赔率', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/agent/generateInviteCodeAndGetInviteRegisterLink', {
				accountType : that.accountTypeLowerLevelOpenAnAccount,
				rebate : that.rebateAndOddsLowerLevelOpenAnAccount.rebate,
				odds : that.rebateAndOddsLowerLevelOpenAnAccount.odds
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.inviteRegisterLink = res.body.data.inviteRegisterLink;
			});
		}
	}
});