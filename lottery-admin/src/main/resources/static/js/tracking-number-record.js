var trackingNumberRecordVM = new Vue({
	el : '#tracking-number-record',
	data : {
		orderNo : '',
		gameDictItems : [],
		gameCode : '',
		startTime : dayjs().format('YYYY-MM-DD'),
		endTime : dayjs().format('YYYY-MM-DD'),
		trackingNumberOrderStateDictItems : [],
		state : ''
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.loadGameDictItem();
		this.loadTrackingNumberOrderStateDictItem();
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

		loadTrackingNumberOrderStateDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'trackingNumberOrderState'
				}
			}).then(function(res) {
				this.trackingNumberOrderStateDictItems = res.body.data;
			});
		},

		initTable : function() {
			var that = this;
			$('.tracking-number-record-table').bootstrapTable({
				classes : 'table table-hover',
				height : 490,
				url : '/trackingNumber/findTrackingNumberSituationByPage',
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
						orderNo : that.orderNo,
						gameCode : that.gameCode,
						startTime : that.startTime,
						endTime : that.endTime,
						state : that.state
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
					title : '订单号',
					cellStyle : {
						classes : 'tracking-number-order-no'
					}
				}, {
					field : 'gameName',
					title : '游戏'
				}, {
					field : 'trackingNumberTime',
					title : '追号时间'
				}, {
					field : 'startIssueNum',
					title : '开始期号'
				}, {
					field : 'totalIssueCount',
					title : '追号期数',
				}, {
					field : 'totalBettingAmount',
					title : '总金额',
					formatter : function(value) {
						return value + '元';
					}
				}, {
					field : 'completedIssueCount',
					title : '完成期数'
				}, {
					field : 'winToStop',
					title : '中奖即停',
					formatter : function(value) {
						return value ? '是' : '否';
					}
				}, {
					field : 'stateName',
					title : '状态',
					cellStyle : {
						classes : 'tracking-number-state'
					}
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						if (row.uncompletedIssueCount > 0) {
							return '<button type="button" class="cancel-order-btn btn btn-outline-danger btn-sm">撤单</button>';
						}
					},
					events : {
						'click .cancel-order-btn' : function(event, value, row, index) {
							that.cancelOrder(row.id);
						}
					}
				} ],
				onClickCell : function(field, value, row) {
					if ('orderNo' == field) {
						trackingNumberOrderDetailsModal.loadAndShowTrackingNumberOrderDetails(row.id);
					}
				}
			});
		},
		refreshTable : function() {
			$('.tracking-number-record-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		cancelOrder : function(orderId) {
			var that = this;
			layer.confirm('确定要撤单吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/trackingNumber/cancelOrder', {
					params : {
						orderId : orderId
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
		}
	}
});