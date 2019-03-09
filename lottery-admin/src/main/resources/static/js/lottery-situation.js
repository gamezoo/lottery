var issueManage = new Vue({
	el : '#issue-manage',
	data : {
		gameDictItems : [],
		gameCode : '',
		issueStateDictItems : [],
		state : '',
		issueNum : '',
		lotteryStartDate : dayjs().format('YYYY-MM-DD'),
		lotteryEndDate : dayjs().format('YYYY-MM-DD'),
		manualLotteryFlag : false,
		autoSettlementFlag : true,
		selectedIssue : {},
		issueEditFlag : false,
		issueInvalid : false
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.loadGameDictItem();
		this.loadIssueStateDictItem();
		this.initTable();
	},
	methods : {

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

		loadIssueStateDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'issueState'
				}
			}).then(function(res) {
				this.issueStateDictItems = res.body.data;
			});
		},

		/**
		 * 初始化表格
		 */
		initTable : function() {
			var that = this;
			$('.lottery-situation-table').bootstrapTable({
				classes : 'table table-hover',
				height : 490,
				url : '/issue/findLotterySituationByPage',
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
						gameCode : that.gameCode,
						issueNum : that.issueNum,
						state : that.state,
						lotteryStartDate : that.lotteryStartDate,
						lotteryEndDate : that.lotteryEndDate
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
					field : 'gameName',
					title : '游戏'
				}, {
					field : 'issueNum',
					title : '期数',
					cellStyle : {
						classes : 'lottery-situation-issue-num'
					}
				}, {
					field : 'lotteryDate',
					title : '日期'
				}, {
					field : 'lotteryNum',
					title : '开奖号码'
				}, {
					field : 'stateName',
					title : '状态'
				}, {
					field : 'lotteryTime',
					title : '预设开奖时间'
				}, {
					field : 'syncTime',
					title : '开奖同步时间'
				}, {
					field : 'settlementTime',
					title : '结算时间'
				}, {
					field : 'totalBettingAmount',
					title : '投注金额'
				}, {
					field : 'totalWinningAmount',
					title : '中奖金额'
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						if (row.state == '1') {
							return '<button type="button" class="manual-lottery-btn btn btn-outline-info btn-sm">手动开奖</button>';
						} else if (row.state == '2') {
							return '<button type="button" class="manual-settlement-btn btn btn-outline-success btn-sm">手动结算</button>';
						}
					},
					events : {
						'click .manual-lottery-btn' : function(event, value, row, index) {
							that.openManualLotteryModal(row.id);
						},
						'click .manual-settlement-btn' : function(event, value, row, index) {
							that.manualSettlement(row.id);
						}
					}
				} ],
				onClickCell : function(field, value, row) {
					if ('issueNum' == field) {
						that.openIssueEditModal(row.id);
					}
				}
			});
		},

		refreshTable : function() {
			$('.lottery-situation-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		openIssueEditModal : function(id) {
			var that = this;
			that.issueEditFlag = true;
			that.selectedIssue = {};
			that.issueInvalid = false;
			that.$http.get('/issue/findLotterySituationById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.selectedIssue = res.body.data;
				that.issueInvalid = that.selectedIssue.state == '4';
			});
		},

		updateIssue : function() {
			var that = this;
			that.$http.post('/issue/updateIssue', {
				id : that.selectedIssue.id,
				automaticLottery : that.selectedIssue.automaticLottery,
				automaticSettlement : that.selectedIssue.automaticSettlement,
				issueInvalid : that.issueInvalid
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.issueEditFlag = false;
				that.refreshTable();
			});
		},

		openManualLotteryModal : function(id) {
			var that = this;
			that.manualLotteryFlag = true;
			that.autoSettlementFlag = true;
			that.selectedIssue = {};
			that.$http.get('/issue/findLotterySituationById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.selectedIssue = res.body.data;
			});
		},

		manualLottery : function() {
			var that = this;
			var selectedIssue = that.selectedIssue;
			if (selectedIssue.lotteryNum == null || selectedIssue.lotteryNum == '') {
				layer.alert('请输入开奖号码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/issue/manualLottery', {
				id : selectedIssue.id,
				lotteryNum : selectedIssue.lotteryNum,
				autoSettlementFlag : that.autoSettlementFlag
			}, {
				emulateJSON : true
			}).then(function(res) {
				var msg = null;
				if (!that.autoSettlementFlag) {
					msg = '操作成功!';
				} else {
					msg = '已通知系统进行结算!';
				}
				layer.alert(msg, {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.manualLotteryFlag = false;
				that.refreshTable();
			});
		},

		manualSettlement : function(id) {
			var that = this;
			layer.confirm('确定要结算吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/issue/manualSettlement', {
					params : {
						id : id
					}
				}).then(function(res) {
					layer.alert('已通知系统进行结算!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.refreshTable();
				});
			});
		}
	}
});