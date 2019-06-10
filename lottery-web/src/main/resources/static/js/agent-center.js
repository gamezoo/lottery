var agentCenterVM = new Vue({
	el : '#agent-center',
	data : {
		currentTab : '',
		rebateAndOddses : [],
		gameDictItems : [],

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

		/**
		 * 团队投注明细相关参数start
		 */
		startTimeBettingDetails : '',
		endTimeBettingDetails : '',
		userNameBettingDetails : '',
		accountTypeBettingDetails : '',
		accountTypeDictItems : [ {
			dictItemCode : 'member',
			dictItemName : '所以下级会员'
		}, {
			dictItemCode : 'agent',
			dictItemName : '所有下级代理'
		} ],
		gameCodeBettingDetails : '',
		gameDictItems : [],
		issueNumBettingDetails : '',
		stateBettingDetails : '',
		bettingOrderStateDictItems : [],

		/**
		 * 团队充值明细相关参数start
		 */
		startTimeRechargeDetails : '',
		endTimeRechargeDetails : '',
		userNameRechargeDetails : '',
		accountTypeRechargeDetails : '',

		/**
		 * 团队提现明细相关参数start
		 */
		startTimeWithdrawDetails : '',
		endTimeWithdrawDetails : '',
		userNameWithdrawDetails : '',
		accountTypeWithdrawDetails : '',

		/**
		 * 团队盈亏报表相关参数start
		 */
		startTimeProfitAndLoss : '',
		endTimeProfitAndLoss : '',
		userNameProfitAndLoss : '',
		accountTypeProfitAndLoss : '',

		/**
		 * 团队帐变报表相关参数start
		 */
		gameCodeAccountChange : '',
		startTimeAccountChange : '',
		endTimeAccountChange : '',
		userNameAccountChange : '',
		typeCodeAccountChange : '',
		accountChangeTypeDictItems : [],

	},
	computed : {},
	created : function() {
	},
	mounted : function() {
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
		loadRebateAndOddsData : function() {
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
			this.loadRebateAndOddsData();
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
			this.loadRebateAndOddsData();
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
		},

		/**
		 * 团队投注明细tab相关方法start
		 */
		switchBettingDetailsTab : function() {
			this.currentTab = 'bettingDetails';
			this.loadGameDictItem();
			this.loadBettingOrderStateDictItem();
			this.resetBettingDetailsQueryCond();
			this.initBettingDetailsTable();
		},

		loadGameDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'game'
				}
			}).then(function(res) {
				this.gameDictItems = res.body.data;
			});
		},

		loadBettingOrderStateDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'bettingOrderState'
				}
			}).then(function(res) {
				this.bettingOrderStateDictItems = res.body.data;
			});
		},

		resetBettingDetailsQueryCond : function() {
			this.startTimeBettingDetails = dayjs().format('YYYY-MM-DD');
			this.endTimeBettingDetails = dayjs().format('YYYY-MM-DD');
			this.userNameBettingDetails = headerVM.userName;
			this.accountTypeBettingDetails = '';
			this.gameCodeBettingDetails = '';
			this.issueNumBettingDetails = '';
			this.stateBettingDetails = '';
		},

		initBettingDetailsTable : function() {
			var that = this;
			$('.betting-details-table').bootstrapTable('destroy');
			$('.betting-details-table').bootstrapTable({
				classes : 'table table-hover',
				height : 540,
				url : '/betting/findLowerLevelBettingOrderInfoByPage',
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
						startTime : that.startTimeBettingDetails,
						endTime : that.endTimeBettingDetails,
						userName : that.userNameBettingDetails,
						accountType : that.accountTypeBettingDetails,
						gameCode : that.gameCodeBettingDetails,
						issueNum : that.issueNumBettingDetails,
						state : that.stateBettingDetails
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
					field : 'userName',
					title : '用户名',
					formatter : function(value) {
						if (value == headerVM.userName) {
							value += '(自己)';
						}
						return value;
					}
				}, {
					field : 'orderNo',
					title : '订单号',
					cellStyle : {
						classes : 'betting-record-order-no'
					}
				}, {
					field : 'bettingTime',
					title : '投注时间'
				}, {
					field : 'gameName',
					title : '游戏'
				}, {
					field : 'issueNum',
					title : '期号'
				}, {
					field : 'stateName',
					title : '状态',
					cellStyle : {
						classes : 'betting-record-state'
					}
				}, {
					field : 'totalBettingAmount',
					title : '投注金额',
					formatter : function(value) {
						return value + '元';
					}
				}, {
					field : 'totalWinningAmount',
					title : '中奖金额',
					formatter : function(value) {
						return value + '元';
					}
				}, {
					field : 'totalProfitAndLoss',
					title : '盈亏',
					formatter : function(value) {
						return value + '元';
					}
				} ],
				onClickCell : function(field, value, row) {
					if ('orderNo' == field) {
						bettingOrderDetailsModal.loadAndShowBettingOrderDetails(row.id);
					}
				}
			});
		},

		refreshBettingDetailsTable : function() {
			$('.betting-details-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		/**
		 * 团队充值明细tab相关方法start
		 */
		switchRechargeDetailsTab : function() {
			this.currentTab = 'rechargeDetails';
			this.resetRechargeDetailsQueryCond();
			this.initRechargeDetailsTable();
		},

		resetRechargeDetailsQueryCond : function() {
			this.startTimeRechargeDetails = dayjs().format('YYYY-MM-DD');
			this.endTimeRechargeDetails = dayjs().format('YYYY-MM-DD');
			this.userNameRechargeDetails = '';
			this.accountTypeRechargeDetails = '';
		},

		initRechargeDetailsTable : function() {
			var that = this;
			$('.recharge-details-table').bootstrapTable('destroy');
			$('.recharge-details-table').bootstrapTable({
				classes : 'table table-hover',
				height : 540,
				url : '/recharge/findLowerLevelRechargeOrderByPage',
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
						submitStartTime : that.startTimeRechargeDetails,
						submitEndTime : that.endTimeRechargeDetails,
						userName : that.userNameRechargeDetails,
						accountType : that.accountTypeRechargeDetails
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
					field : 'userName',
					title : '用户名',
					formatter : function(value) {
						if (value == headerVM.userName) {
							value += '(自己)';
						}
						return value;
					}
				}, {
					field : 'orderNo',
					title : '订单号'
				}, {
					field : 'submitTime',
					title : '充值时间'
				}, {
					field : 'rechargeAmount',
					title : '充值金额'
				}, {
					field : 'rechargeWayName',
					title : '充值方式'
				}, {
					field : 'orderStateName',
					title : '状态'
				} ]
			});
		},

		refreshRechargeDetailsTable : function() {
			$('.recharge-details-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		/**
		 * 团队提现明细tab相关方法start
		 */
		switchWithdrawDetailsTab : function() {
			this.currentTab = 'withdrawDetails';
			this.resetWithdrawDetailsQueryCond();
			this.initWithdrawDetailsTable();
		},

		resetWithdrawDetailsQueryCond : function() {
			this.startTimeWithdrawDetails = dayjs().format('YYYY-MM-DD');
			this.endTimeWithdrawDetails = dayjs().format('YYYY-MM-DD');
			this.userNameWithdrawDetails = '';
			this.accountTypeWithdrawDetails = '';
		},

		initWithdrawDetailsTable : function() {
			var that = this;
			$('.withdraw-details-table').bootstrapTable('destroy');
			$('.withdraw-details-table').bootstrapTable({
				classes : 'table table-hover',
				height : 540,
				url : '/withdraw/findLowerLevelWithdrawRecordByPage',
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
						submitStartTime : that.startTimeWithdrawDetails,
						submitEndTime : that.endTimeWithdrawDetails,
						userName : that.userNameWithdrawDetails,
						accountType : that.accountTypeWithdrawDetails
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
					field : 'userName',
					title : '用户名',
					formatter : function(value) {
						if (value == headerVM.userName) {
							value += '(自己)';
						}
						return value;
					}
				}, {
					field : 'orderNo',
					title : '订单号'
				}, {
					field : 'submitTime',
					title : '申请提现时间'
				}, {
					field : 'withdrawAmount',
					title : '提现金额'
				}, {
					field : 'stateName',
					title : '状态'
				} ]
			});
		},

		refreshWithdrawDetailsTable : function() {
			$('.withdraw-details-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		/**
		 * 团队盈亏报表tab相关方法start
		 */
		switchProfitAndLossTab : function() {
			this.currentTab = 'profitAndLoss';
			this.resetProfitAndLossQueryCond();
			this.initProfitAndLossTable();
		},

		resetProfitAndLossQueryCond : function() {
			this.startTimeProfitAndLoss = dayjs().format('YYYY-MM-DD');
			this.endTimeProfitAndLoss = dayjs().format('YYYY-MM-DD');
			this.userNameProfitAndLoss = '';
			this.accountTypeProfitAndLoss = '';
		},

		initProfitAndLossTable : function() {
			var that = this;
			$('.profit-and-loss-table').bootstrapTable('destroy');
			$('.profit-and-loss-table').bootstrapTable({
				classes : 'table table-hover',
				height : 540,
				url : '/agent/findAccountProfitAndLossByPage',
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
						startTime : that.startTimeProfitAndLoss,
						endTime : that.endTimeProfitAndLoss,
						userName : that.userNameProfitAndLoss,
						accountType : that.accountTypeProfitAndLoss
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
					field : 'rechargeAmount',
					title : '充值总额'
				}, {
					field : 'withdrawAmount',
					title : '提现总额'
				}, {
					field : 'totalBettingAmount',
					title : '彩票投注'
				}, {
					field : 'totalWinningAmount',
					title : '彩票返奖'
				}, {
					field : 'rebateAmount',
					title : '彩票返点'
				}, {
					field : 'lowerLevelRebateAmount',
					title : '下级返点'
				}, {
					field : 'balance',
					title : '余额'
				}, {
					field : 'bettingProfitAndLoss',
					title : '投注盈亏'
				} ]
			});
		},

		refreshProfitAndLossTable : function() {
			$('.profit-and-loss-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		/**
		 * 团队帐变报表tab相关方法start
		 */
		switchAccountChangeTab : function() {
			this.currentTab = 'accountChange';
			this.loadGameDictItem();
			this.loadAccountChangeTypeDictItem();
			this.resetAccountChangeQueryCond();
			this.initAccountChangeTable();
		},

		/**
		 * 加载账变类型字典项
		 */
		loadAccountChangeTypeDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'accountChangeType'
				}
			}).then(function(res) {
				this.accountChangeTypeDictItems = res.body.data;
			});
		},

		resetAccountChangeQueryCond : function() {
			this.gameCodeAccountChange = '';
			this.startTimeAccountChange = dayjs().format('YYYY-MM-DD');
			this.endTimeAccountChange = dayjs().format('YYYY-MM-DD');
			this.userNameAccountChange = '';
			this.typeCodeAccountChange = '';
		},

		initAccountChangeTable : function() {
			var that = this;
			$('.account-change-table').bootstrapTable('destroy');
			$('.account-change-table').bootstrapTable({
				classes : 'table table-hover',
				height : 540,
				url : '/userAccount/findLowerLevelAccountChangeLogByPage',
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
						gameCode : that.gameCodeAccountChange,
						startTime : that.startTimeAccountChange,
						endTime : that.endTimeAccountChange,
						userName : that.userNameAccountChange,
						accountChangeTypeCode : that.typeCodeAccountChange
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
					field : 'userName',
					title : '用户名',
					formatter : function(value) {
						if (value == headerVM.userName) {
							value += '(自己)';
						}
						return value;
					}
				}, {
					field : 'orderNo',
					title : '订单号'
				}, {
					field : 'gameName',
					title : '游戏'
				}, {
					field : 'issueNum',
					title : '期号'
				}, {
					field : 'accountChangeTime',
					title : '账变时间'
				}, {
					field : 'accountChangeTypeName',
					title : '类型'
				}, {
					field : 'accountChangeAmount',
					title : '账变金额'
				}, {
					field : 'balance',
					title : '余额'
				} ]
			});
		},

		refreshAccountChangeTable : function() {
			$('.account-change-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		}
	}
});