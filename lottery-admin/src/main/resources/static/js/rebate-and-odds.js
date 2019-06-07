var rebateAndOddsVM = new Vue({
	el : '#rebate-and-odds',
	data : {
		userName : '',
		ipAddr : '',
		state : '',
		loginStateDictItems : [],
		startTime : dayjs().format('YYYY-MM-DD'),
		endTime : dayjs().format('YYYY-MM-DD'),

		quickSettingFlag : false,
		lowestRebate : null,
		highestRebate : null,
		rebateStep : null,
		lowestOdds : null,
		highestOdds : null,
		oddsStep : null,
		rebateAndOddses : [],

		editRebateAndOdds : {},
		addOrUpdateRebateAndOddsFlag : false,
		rebateAndOddsActionTitle : '',
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.loadLoginStateDictItem();
		this.initTable();
	},
	methods : {
		loadLoginStateDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'loginState'
				}
			}).then(function(res) {
				this.loginStateDictItems = res.body.data;
			});
		},

		initTable : function() {
			var that = this;
			$('.rebate-and-odds-table').bootstrapTable({
				classes : 'table table-hover',
				height : 490,
				url : '/agent/findRebateAndOddsSituationByPage',
				pagination : true,
				sidePagination : 'server',
				pageNumber : 1,
				pageSize : 50,
				pageList : [ 10, 25, 50, 100 ],
				queryParamsType : '',
				queryParams : function(params) {
					var condParam = {
						pageSize : params.pageSize,
						pageNum : params.pageNumber,
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
					title : '返点/赔率',
					formatter : function(value, row, index) {
						return row.rebate + '%/' + row.odds;
					}
				}, {
					title : '状态',
					formatter : function(value, row, index) {
						if (row.createTime != null) {
							return '正常';
						}
						return '旧的返点/赔率';
					},
					cellStyle : function(value, row, index) {
						if (row.createTime == null) {
							return {
								classes : 'rebate-and-odds-state'
							};
						}
						return {};
					}
				}, {
					field : 'associatedAccountNum',
					title : '分配账号数量',
					formatter : function(value) {
						if (value > 0) {
							return value;
						}
						return '未分配';
					}
				}, {
					field : 'createTime',
					title : '创建时间'
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						if (row.createTime == null) {
							return;
						}
						return [ '<button type="button" class="edit-rebate-and-odds-btn btn btn-outline-primary btn-sm" style="margin-right: 4px;">编辑</button>', '<button type="button" class="del-rebate-and-odds-btn btn btn-outline-danger btn-sm">删除</button>' ].join('');
					},
					events : {
						'click .edit-rebate-and-odds-btn' : function(event, value, row, index) {
							that.showEditRebateAndOddsModal(row);
						},
						'click .del-rebate-and-odds-btn' : function(event, value, row, index) {
							that.delRebateAndOdds(row);
						}
					}
				} ]
			});
		},
		refreshTable : function() {
			$('.rebate-and-odds-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		showAddRebateAndOddsModal : function() {
			this.editRebateAndOdds = {
				rebate : null,
				odds : null
			};
			this.addOrUpdateRebateAndOddsFlag = true;
			this.rebateAndOddsActionTitle = '新增返点/赔率';
		},

		showEditRebateAndOddsModal : function(rebateAndOdds) {
			var that = this;
			that.$http.get('/agent/findRebateAndOdds', {
				params : {
					rebate : rebateAndOdds.rebate,
					odds : rebateAndOdds.odds
				}
			}).then(function(res) {
				that.editRebateAndOdds = res.body.data;
				that.addOrUpdateRebateAndOddsFlag = true;
				that.rebateAndOddsActionTitle = '编辑返点/赔率';
			});
		},

		addOrUpdateRebateAndOdds : function() {
			var that = this;
			var editRebateAndOdds = that.editRebateAndOdds;
			if (editRebateAndOdds.rebate == null || editRebateAndOdds.rebate === '') {
				layer.alert('请输入返点', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editRebateAndOdds.odds == null || editRebateAndOdds.odds == '') {
				layer.alert('请输入赔率', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/agent/addOrUpdateRebateAndOdds', editRebateAndOdds).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addOrUpdateRebateAndOddsFlag = false;
				that.refreshTable();
			});
		},

		delRebateAndOdds : function(rebateAndOdds) {
			var that = this;
			layer.confirm('确定要删除吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/agent/delRebateAndOdds', {
					params : {
						rebate : rebateAndOdds.rebate,
						odds : rebateAndOdds.odds
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.refreshTable();
				});
			});
		},

		showQuickSettingModal : function() {
			this.quickSettingFlag = true;
			this.lowestRebate = null;
			this.highestRebate = null;
			this.rebateStep = null;
			this.lowestOdds = null;
			this.highestOdds = null;
			this.oddsStep = null;
			this.rebateAndOddses = [];
		},

		addRecord : function() {
			this.rebateAndOddses.push({
				rebate : null,
				odds : null
			});
		},

		generateRecord : function() {
			if (this.lowestRebate == null || this.lowestRebate === '') {
				layer.alert('请输入最低返点', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (this.highestRebate == null || this.highestRebate === '') {
				layer.alert('请输入最高返点', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (this.rebateStep == null || this.rebateStep === '') {
				layer.alert('请输入返点步长', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (this.lowestRebate >= this.highestRebate) {
				layer.alert('最低返点必须要小于最高返点', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (this.lowestOdds == null || this.lowestOdds === '') {
				layer.alert('请输入最低赔率', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (this.highestOdds == null || this.highestOdds === '') {
				layer.alert('请输入最高赔率', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (this.oddsStep == null || this.oddsStep === '') {
				layer.alert('请输入赔率步长', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (this.lowestOdds >= this.highestOdds) {
				layer.alert('最低赔率必须要小于最高赔率', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var rebates = [];
			for (var i = this.lowestRebate; i <= this.highestRebate; i = numberFormat(i + this.rebateStep)) {
				rebates.push(numberFormat(i));
			}
			var oddses = [];
			for (var i = this.lowestOdds; i <= this.highestOdds; i = numberFormat(i + this.oddsStep)) {
				oddses.push(numberFormat(i));
			}
			if (rebates.length != oddses.length) {
				layer.alert('返点/赔率生成的记录长度不一致,请重新调整', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (rebates.length == 0) {
				layer.alert('返点/赔率生成的记录长度为0,请重新调整', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var rebateAndOddses = [];
			for (var i = 0; i < rebates.length; i++) {
				rebateAndOddses.push({
					rebate : rebates[i],
					odds : oddses[i],
				});
			}
			this.rebateAndOddses = rebateAndOddses;
		},

		saveRebateAndOdds : function() {
			var that = this;
			if (this.rebateAndOddses.length == 0) {
				layer.alert('请增加返点/赔率记录', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var map = new Map();
			for (var i = 0; i < this.rebateAndOddses.length; i++) {
				var rebateAndOdd = this.rebateAndOddses[i];
				if (rebateAndOdd.rebate == null || rebateAndOdd.rebate === '') {
					layer.alert('请输入返点', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (rebateAndOdd.odds == null || rebateAndOdd.odds === '') {
					layer.alert('请输入赔率', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				var key = rebateAndOdd.rebate + '/' + rebateAndOdd.odds;
				if (map.get(key)) {
					layer.alert('不能设置重复的返点/赔率', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				map.set(key, key);
			}
			that.$http.post('/agent/resetRebateAndOdds', this.rebateAndOddses).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.quickSettingFlag = false;
				that.refreshTable();
			});
		},
	}
});