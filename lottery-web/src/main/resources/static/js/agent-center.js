var personalCenter = new Vue({
	el : '#personal-center',
	data : {
		currentTab : '',
		rebateAndOddses : [],

		/**
		 * 代理开户相关参数start
		 */
		userNameAgentOpenAnAccount : '',
		loginPwdAgentOpenAnAccount : '',
		confirmLoginPwdAgentOpenAnAccount : '',
		rebateAndOddsAgentOpenAnAccount : '',

	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.initRebateAndOddsData();
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
				
			});
		}
	}
});