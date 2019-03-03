var rechargeOrderVM = new Vue({
	el : '#recharge-order',
	data : {
		orderNo : '',
		rechargeWayCode : '',
		rechargeWayDictItems : [],
		orderState : '',
		rechargeOrderStateDictItems : [],
		submitStartTime : dayjs().format('YYYY-MM-DD'),
		submitEndTime : dayjs().format('YYYY-MM-DD')
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.loadRechargeWayDictItem();
		this.loadRechargeOrderStateDictItem();
		this.initTable();
	},
	methods : {
		/**
		 * 加载充值方式字典项
		 */
		loadRechargeWayDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'rechargeWay'
				}
			}).then(function(res) {
				this.rechargeWayDictItems = res.body.data;
			});
		},

		/**
		 * 加载充值订单状态字典项
		 */
		loadRechargeOrderStateDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'rechargeOrderState'
				}
			}).then(function(res) {
				this.rechargeOrderStateDictItems = res.body.data;
			});
		},

		initTable : function() {
			var that = this;
			$('.recharge-order-table').bootstrapTable({
				classes : 'table table-hover',
				height : 490,
				url : '/recharge/findRechargeOrderByPage',
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
						rechargeWayCode : that.rechargeWayCode,
						orderState : that.orderState,
						submitStartTime : that.submitStartTime,
						submitEndTime : that.submitEndTime
					};
					return condParam;
				},
				responseHandler : function(res) {
					return {
						total : res.data.total,
						rows : res.data.content
					};
				},
				detailView : true,
				detailFormatter : function(index, row, element) {
					var html = template('recharge-order-detail', {
						rechargeOrderInfo : row
					});
					return html;
				},
				columns : [ {
					field : 'orderNo',
					title : '订单号'
				}, {
					field : 'userName',
					title : '充值用户'
				}, {
					field : 'orderStateName',
					title : '订单状态'
				}, {
					field : 'rechargeAmount',
					title : '充值金额'
				}, {
					field : 'rechargeWayName',
					title : '充值方式'
				}, {
					field : 'submitTime',
					title : '提交时间'
				}, {
					field : 'payTime',
					title : '支付时间'
				}, {
					field : 'settlementTime',
					title : '结算时间'
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						if (row.orderState == '1') {
							return [ '<button type="button" class="cancel-order-btn btn btn-outline-danger btn-sm">取消订单</button>' ].join('');
						}
					},
					events : {
						'click .cancel-order-btn' : function(event, value, row, index) {
							that.cancelOrder(row.id);
						}
					}
				} ]
			});
		},

		refreshTable : function() {
			$('.recharge-order-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		cancelOrder : function(id) {
			var that = this;
			layer.confirm('确定要取消订单吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/recharge/cancelOrder', {
					params : {
						id : id
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