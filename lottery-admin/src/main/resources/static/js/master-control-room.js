var masterControlRoomVM = new Vue({
	el : '#master-control-room',
	data : {
		/**
		 * 邀请注册start
		 */
		inviteCodeEffectiveDuration : '',
		inviteRegisterEnabled : false,
		/**
		 * 新用户注册礼金start
		 */
		registerAmount : '',
		registerAmountEnabled : false,
		/**
		 * 充值start
		 */
		rechargeOrderEffectiveDuration : '',
		rechargeReturnWaterRate : '',
		rechargeReturnWaterRateEnabled : false,
		/**
		 * 刷新缓存start
		 */
		refreshConfigItem : true,
		refreshDict : true
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.loadInviteRegisterSetting();
		this.loadRegisterAmountSetting();
		this.loadRechargeSetting();
	},
	methods : {

		loadInviteRegisterSetting : function() {
			var that = this;
			that.$http.get('/masterControl/getInviteRegisterSetting').then(function(res) {
				if (res.body.data != null) {
					that.inviteCodeEffectiveDuration = res.body.data.effectiveDuration;
					that.inviteRegisterEnabled = res.body.data.enabled;
				}
			});
		},

		updateInviteRegisterSetting : function() {
			var that = this;
			var inviteCodeEffectiveDuration = that.inviteCodeEffectiveDuration;
			if (inviteCodeEffectiveDuration == null || inviteCodeEffectiveDuration == '') {
				layer.alert('请输入邀请码有效时长', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var inviteRegisterEnabled = that.inviteRegisterEnabled;
			if (inviteRegisterEnabled == null) {
				layer.alert('请选择是否开启', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/masterControl/updateInviteRegisterSetting', {
				effectiveDuration : inviteCodeEffectiveDuration,
				enabled : inviteRegisterEnabled,
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.loadInviteRegisterSetting();
			});
		},

		loadRegisterAmountSetting : function() {
			var that = this;
			that.$http.get('/masterControl/getRegisterAmountSetting').then(function(res) {
				if (res.body.data != null) {
					that.registerAmount = res.body.data.registerAmount;
					that.registerAmountEnabled = res.body.data.enabled;
				}
			});
		},

		updateRegisterAmountSetting : function() {
			var that = this;
			var registerAmount = that.registerAmount;
			if (registerAmount == null || registerAmount == '') {
				layer.alert('请输入注册礼金', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var registerAmountEnabled = that.registerAmountEnabled;
			if (registerAmountEnabled == null) {
				layer.alert('请选择是否启用', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/masterControl/updateRegisterAmountSetting', {
				registerAmount : registerAmount,
				enabled : registerAmountEnabled,
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.loadRegisterAmountSetting();
			});
		},

		loadRechargeSetting : function() {
			var that = this;
			that.$http.get('/masterControl/getRechargeSetting').then(function(res) {
				if (res.body.data != null) {
					that.rechargeOrderEffectiveDuration = res.body.data.orderEffectiveDuration;
					that.rechargeReturnWaterRate = res.body.data.returnWaterRate;
					that.rechargeReturnWaterRateEnabled = res.body.data.returnWaterRateEnabled;
				}
			});
		},

		updateRechargeSetting : function() {
			var that = this;
			var rechargeOrderEffectiveDuration = that.rechargeOrderEffectiveDuration;
			if (rechargeOrderEffectiveDuration == null || rechargeOrderEffectiveDuration == '') {
				layer.alert('请输入充值订单有效时长', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var rechargeReturnWaterRate = that.rechargeReturnWaterRate;
			if (rechargeReturnWaterRate == null || rechargeReturnWaterRate == '') {
				layer.alert('请输入充值返水率', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var rechargeReturnWaterRateEnabled = that.rechargeReturnWaterRateEnabled;
			if (rechargeReturnWaterRateEnabled == null) {
				layer.alert('请选择是否启用', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}

			that.$http.post('/masterControl/updateRechargeSetting', {
				orderEffectiveDuration : rechargeOrderEffectiveDuration,
				returnWaterRate : rechargeReturnWaterRate,
				returnWaterRateEnabled : rechargeReturnWaterRateEnabled,
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.loadRechargeSetting();
			});
		},

		refreshCache : function() {
			var cacheItems = [];
			var that = this;
			if (that.refreshConfigItem) {
				cacheItems.push('config*');
			}
			if (that.refreshConfigItem) {
				cacheItems.push('dict*');
			}
			if (cacheItems.length == 0) {
				layer.alert('请勾选要刷新的缓存项', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/masterControl/refreshCache', cacheItems).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
			});
		}
	}
});