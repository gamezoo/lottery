var issueManage = new Vue({
	el : '#issue-manage',
	data : {
		gameDictItems : [],
		gameCode : '',
		issueStateDictItems : [],
		state : '',
		issueNum : '',
		lotteryDate : dayjs().format('YYYY-MM-DD')
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
			that.$http.get('/dict/findDictItemInCache', {
				params : {
					dictTypeCode : 'game'
				}
			}).then(function(res) {
				this.gameDictItems = res.body.data;
			});
		},

		loadIssueStateDictItem : function() {
			var that = this;
			that.$http.get('/dict/findDictItemInCache', {
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
						lotteryDate : that.lotteryDate
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
					title : '期数'
				}, {
					field : 'lotteryDate',
					title : '日期'
				}, {
					field : 'lotteryNum',
					title : '开奖结果'
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
					title : '操作'
				} ]
			});

		},

		refreshTable : function() {
			$('.lottery-situation-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		}

	}
});