var personalCenter = new Vue({
	el : '#personal-center',
	data : {
		currentTab : '',
		gameDictItems : [],

		/**
		 * 银行卡资料管理tab相关参数start
		 */
		editBankInfoFlag : false,
		bankInfo : {},
		openAccountBank : '',
		accountHolder : '',
		bankCardAccount : '',

		/**
		 * 登录密码修改tab相关参数start
		 */
		oldLoginPwd : '',
		newLoginPwd : '',
		confirmLoginPwd : '',

		/**
		 * 资金密码修改tab相关参数start
		 */
		oldMoneyPwd : '',
		newMoneyPwd : '',
		confirmMoneyPwd : '',

		/**
		 * 个人账变start
		 */
		accountChangeGameCode : '',
		accountChangeStartTime : dayjs().format('YYYY-MM-DD'),
		accountChangeEndTime : dayjs().format('YYYY-MM-DD'),
		accountChangeTypeCode : '',
		accountChangeTypeDictItems : [],

		/**
		 * 个人充提start
		 */
		rechargeWithdrawOrderType : '',
		rechargeWithdrawStartTime : dayjs().format('YYYY-MM-DD'),
		rechargeWithdrawEndTime : dayjs().format('YYYY-MM-DD'),
		rechargeWithdrawOrderTypeDictItems : []
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.switchAccountChangeTab();
	},
	methods : {

		/**
		 * 银行卡资料管理tab相关方法start
		 */
		switchBankCardInfoTab : function() {
			this.currentTab = 'bankCardInfo';
			this.editBankInfoFlag = false;
			this.loadBankInfo();
		},

		loadBankInfo : function() {
			var that = this;
			that.$http.get('/userAccount/getBankInfo').then(function(res) {
				that.bankInfo = res.body.data;
			});
		},

		editBankInfo : function() {
			this.editBankInfoFlag = true;
			this.openAccountBank = this.bankInfo.openAccountBank;
			this.accountHolder = this.bankInfo.accountHolder;
			this.bankCardAccount = this.bankInfo.bankCardAccount;
		},

		bindBankInfo : function() {
			var that = this;
			if (that.openAccountBank == null || that.openAccountBank == '') {
				layer.alert('请输入开户银行', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.accountHolder == null || that.accountHolder == '') {
				layer.alert('请输入开户人姓名', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.bankCardAccount == null || that.bankCardAccount == '') {
				layer.alert('请输入银行卡账号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/userAccount/bindBankInfo', {
				openAccountBank : that.openAccountBank,
				accountHolder : that.accountHolder,
				bankCardAccount : that.bankCardAccount
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.confirm('银行卡信息绑定成功!', {
					icon : 1,
					btn : [ '确定' ]
				}, function() {
					window.location.reload();
				});
			});
		},

		/**
		 * 登录密码修改tab相关方法start
		 */
		switchLoginPwdModifyTab : function() {
			this.currentTab = 'loginPwdModify';
			this.resetLoginPwdModifyTabData();
		},

		resetLoginPwdModifyTabData : function() {
			this.oldLoginPwd = '';
			this.newLoginPwd = '';
			this.confirmLoginPwd = '';
		},

		/**
		 * 修改登录密码
		 */
		modifyLoginPwd : function() {
			var that = this;
			if (that.oldLoginPwd == null || that.oldLoginPwd == '') {
				layer.alert('请输入旧的登录密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.newLoginPwd == null || that.newLoginPwd == '') {
				layer.alert('请输入新的登录密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.confirmLoginPwd == null || that.confirmLoginPwd == '') {
				layer.alert('请输入确认登录密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.newLoginPwd != that.confirmLoginPwd) {
				layer.alert('密码不一致', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/userAccount/modifyLoginPwd', {
				oldLoginPwd : that.oldLoginPwd,
				newLoginPwd : that.newLoginPwd
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.confirm('登录密码修改成功!', {
					icon : 1,
					btn : [ '确定' ]
				}, function() {
					window.location.reload();
				});
			});
		},

		/**
		 * 资金密码修改tab相关方法start
		 */
		switchMoneyPwdModifyTab : function() {
			this.currentTab = 'moneyPwdModify';
			this.resetMoneyPwdModifyTabData();
		},

		resetMoneyPwdModifyTabData : function() {
			this.oldMoneyPwd = '';
			this.newMoneyPwd = '';
			this.confirmMoneyPwd = '';
		},

		/**
		 * 修改资金密码
		 */
		modifyMoneyPwd : function() {
			var that = this;
			if (that.oldMoneyPwd == null || that.oldMoneyPwd == '') {
				layer.alert('请输入旧的资金密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.newMoneyPwd == null || that.newMoneyPwd == '') {
				layer.alert('请输入新的资金密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.confirmMoneyPwd == null || that.confirmMoneyPwd == '') {
				layer.alert('请输入确认资金密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.newMoneyPwd != that.confirmMoneyPwd) {
				layer.alert('密码不一致', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/userAccount/modifyMoneyPwd', {
				oldMoneyPwd : that.oldMoneyPwd,
				newMoneyPwd : that.newMoneyPwd
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.confirm('资金密码修改成功!', {
					icon : 1,
					btn : [ '确定' ]
				}, function() {
					window.location.reload();
				});
			});
		},

		/**
		 * 个人账变相关方法start
		 */

		/**
		 * 切换到个人帐变页面
		 */
		switchAccountChangeTab : function() {
			this.currentTab = 'accountChange';
			this.resetAccountChangeQueryCond();
			this.loadGameDictItem();
			this.loadAccountChangeTypeDictItem();
			this.initAccountChangeTable();
		},

		resetAccountChangeQueryCond : function() {
			this.accountChangeGameCode = '';
			this.accountChangeStartTime = dayjs().format('YYYY-MM-DD');
			this.accountChangeEndTime = dayjs().format('YYYY-MM-DD');
			this.accountChangeTypeCode = '';
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

		/**
		 * 初始化账变表格
		 */
		initAccountChangeTable : function() {
			var that = this;
			$('.account-change-table').bootstrapTable('destroy');
			$('.account-change-table').bootstrapTable({
				classes : 'table table-hover',
				height : 540,
				url : '/userAccount/findMyAccountChangeLogByPage',
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
						gameCode : that.accountChangeGameCode,
						startTime : that.accountChangeStartTime,
						endTime : that.accountChangeEndTime,
						accountChangeTypeCode : that.accountChangeTypeCode
					};
					return condParam;
				},
				responseHandler : function(res) {
					return {
						total : res.data.total,
						rows : res.data.content
					};
				},
				columns : [ {
					field : 'userName',
					title : '用户名'
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
		},

		/**
		 * 个人充提相关方法start
		 */

		/**
		 * 切换到个人帐变页面
		 */
		switchRechargeWithdrawTab : function() {
			this.currentTab = 'rechargeWithdraw';
			this.resetRechargeWithdrawQueryCond();
			this.loadRechargeWithdrawOrderTypeDictItem();
			this.initRechargeWithdrawTable();
		},

		resetRechargeWithdrawQueryCond : function() {
			this.rechargeWithdrawOrderType = '';
			this.rechargeWithdrawStartTime = dayjs().format('YYYY-MM-DD');
			this.rechargeWithdrawEndTime = dayjs().format('YYYY-MM-DD');
		},

		/**
		 * 加载充提订单类型字典项
		 */
		loadRechargeWithdrawOrderTypeDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'rechargeWithdrawLogOrderType'
				}
			}).then(function(res) {
				this.rechargeWithdrawOrderTypeDictItems = res.body.data;
			});
		},

		/**
		 * 初始化充提表格
		 */
		initRechargeWithdrawTable : function() {
			var that = this;
			$('.recharge-withdraw-table').bootstrapTable('destroy');
			$('.recharge-withdraw-table').bootstrapTable({
				classes : 'table table-hover',
				height : 540,
				url : '/rechargeWithdrawLog/findMyRechargeWithdrawLogByPage',
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
						orderType : that.rechargeWithdrawOrderType,
						startTime : that.rechargeWithdrawStartTime,
						endTime : that.rechargeWithdrawEndTime
					};
					return condParam;
				},
				responseHandler : function(res) {
					return {
						total : res.data.total,
						rows : res.data.content
					};
				},
				columns : [ {
					field : 'orderNo',
					title : '订单号'
				}, {
					field : 'orderTypeName',
					title : '订单类型'
				}, {
					field : 'orderStateName',
					title : '订单状态'
				}, {
					field : 'submitTime',
					title : '提交时间'
				} ]
			});
		},

		refreshRechargeWithdrawTable : function() {
			$('.recharge-withdraw-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
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
		}
	}
});