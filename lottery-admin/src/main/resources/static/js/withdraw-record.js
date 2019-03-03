var withdrawRecordVM = new Vue({
	el : '#withdraw-record',
	data : {
		orderNo : '',
		state : '',
		withdrawRecordStateDictItems : [],
		submitStartTime : dayjs().format('YYYY-MM-DD'),
		submitEndTime : dayjs().format('YYYY-MM-DD'),
		withdrawApprovalFlag : false,
		onlyShowNotApprovedBtnFlag : false,
		selectedWithdrawRecord : {},
		note : ''
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.loadWithdrawRecordStateDictItem();
		this.initTable();
	},
	methods : {

		/**
		 * 加载提现记录状态字典项
		 */
		loadWithdrawRecordStateDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'withdrawRecordState'
				}
			}).then(function(res) {
				this.withdrawRecordStateDictItems = res.body.data;
			});
		},

		initTable : function() {
			var that = this;
			$('.withdraw-record-table').bootstrapTable({
				classes : 'table table-hover',
				height : 490,
				url : '/withdraw/findWithdrawRecordByPage',
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
						state : that.state,
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
					var html = template('withdraw-record-detail', {
						withdrawRecord : row
					});
					return html;
				},
				columns : [ {
					field : 'orderNo',
					title : '订单号'
				}, {
					field : 'userName',
					title : '发起用户'
				}, {
					field : 'withdrawAmount',
					title : '提现金额'
				}, {
					field : 'openAccountBank',
					title : '开户银行'
				}, {
					field : 'accountHolder',
					title : '开户人姓名'
				}, {
					field : 'bankCardAccount',
					title : '银行卡账号'
				}, {
					field : 'submitTime',
					title : '提交时间'
				}, {
					field : 'stateName',
					title : '状态'
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						if (row.state == '1') {
							return [ '<button type="button" class="withdraw-approval-btn btn btn-outline-primary btn-sm" style="margin-right: 4px;">进行审核</button>' ].join('');
						} else if (row.state == '2') {
							return [ '<button type="button" class="confirm-credited-btn btn btn-outline-primary btn-sm" style="margin-right: 4px;">确认到帐</button>', '<button type="button" class="not-approved-btn btn btn-outline-secondary btn-sm">审核不通过</button>' ].join('');
						}
					},
					events : {
						'click .withdraw-approval-btn' : function(event, value, row, index) {
							that.onlyShowNotApprovedBtnFlag = false;
							that.showWithdrawApprovalModal(row);
						},
						'click .confirm-credited-btn' : function(event, value, row, index) {
							that.selectedWithdrawRecord = row;
							that.confirmCredited();
						},
						'click .not-approved-btn' : function(event, value, row, index) {
							that.onlyShowNotApprovedBtnFlag = true;
							that.showWithdrawApprovalModal(row);
						}
					}
				} ]
			});
		},

		refreshTable : function() {
			$('.withdraw-record-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		showWithdrawApprovalModal : function(row) {
			this.withdrawApprovalFlag = true;
			this.note = null;
			this.selectedWithdrawRecord = row;
		},

		approved : function() {
			var that = this;
			layer.confirm('确定审核通过吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/withdraw/approved', {
					params : {
						id : that.selectedWithdrawRecord.id,
						note : that.note
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.withdrawApprovalFlag = false;
					that.refreshTable();
				});
			});
		},

		notApproved : function() {
			var that = this;
			layer.confirm('确定审核不通过吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/withdraw/notApproved', {
					params : {
						id : that.selectedWithdrawRecord.id,
						note : that.note
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.withdrawApprovalFlag = false;
					that.refreshTable();
				});
			});
		},

		confirmCredited : function(id) {
			var that = this;
			layer.confirm('确认是已到帐了吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/withdraw/confirmCredited', {
					params : {
						id : that.selectedWithdrawRecord.id,
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