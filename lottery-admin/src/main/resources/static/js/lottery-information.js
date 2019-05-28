var lotteryInformationVM = new Vue({
	el : '#lottery-information',
	data : {
		showLotteryInformationFlag : true,
		source : '',
		title : '',
		systemNoticeActionTitle : '',
		noticeContentEditor : null,
		addOrUpdateSystemNoticeFlag : false,
		editSystemNotice : {},

		showInformationCrawlerFlag : false,
		informationCrawlers : [],
		addOrUpdateCrawlerFlag : false,
		crawlerActionTitle : '',
		editCrawler : {},

		showCollectionInformationFlag : false,
		selectedCrawler : {},
		informations : [],
		selectedInformation : {}
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.initTable();
		this.noticeContentEditor = new window.wangEditor('#noticeContentEditor');
		this.noticeContentEditor.customConfig.menus = [ 'head', // 标题
		'bold', // 粗体
		'fontSize', // 字号
		'fontName', // 字体
		'italic', // 斜体
		'underline', // 下划线
		'strikeThrough', // 删除线
		'foreColor', // 文字颜色
		'backColor', // 背景颜色
		'link', // 插入链接
		'list', // 列表
		'justify', // 对齐方式
		'quote', // 引用
		'emoticon', // 表情
		'table', // 表格
		'video', // 插入视频
		'code', // 插入代码
		'undo', // 撤销
		'redo' // 重复
		];
		this.noticeContentEditor.create();
	},
	methods : {

		syncInformation : function() {
			var that = this;
			that.$http.post('/lotteryInformation/syncInformation', that.informations).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
			});
		},

		collectionInformation : function(crawler) {
			var that = this;
			that.informations = [];
			that.selectedInformation = null;
			that.selectedCrawler = crawler;
			that.showInformationCrawlerFlag = false;
			that.showCollectionInformationFlag = true;
			layer.msg('正在采集资讯...', {
				icon : 16,
				shade : 0.01
			});
			that.$http.get('/lotteryInformation/collectionInformation', {
				params : {
					id : crawler.id
				}
			}).then(function(res) {
				that.informations = res.body.data;
				layer.closeAll();
			});
		},

		addOrUpdateCrawler : function() {
			var that = this;
			var editCrawler = that.editCrawler;
			if (editCrawler.source == null || editCrawler.source == '') {
				layer.alert('请输入来源', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editCrawler.script == null || editCrawler.script == '') {
				layer.alert('请输入脚本', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/lotteryInformation/addOrUpdateInformationCrawler', editCrawler).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addOrUpdateCrawlerFlag = false;
				that.toInformationCrawlerPage();
			});
		},

		showAddCrawlerModal : function() {
			this.addOrUpdateCrawlerFlag = true;
			this.crawlerActionTitle = '新增爬虫';
			this.editCrawler = {
				source : '',
				script : ''
			};
		},

		showEditCrawlerModal : function(id) {
			var that = this;
			that.$http.get('/lotteryInformation/findInformationCrawlerById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.editCrawler = res.body.data;
				that.addOrUpdateCrawlerFlag = true;
				that.crawlerActionTitle = '编辑爬虫';
			});
		},

		delCrawler : function(id) {
			var that = this;
			layer.confirm('确定要删除吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/lotteryInformation/delInformationCrawlerById', {
					params : {
						id : id
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.toInformationCrawlerPage();
				});
			});
		},

		toInformationCrawlerPage : function() {
			var that = this;
			that.$http.get('/lotteryInformation/findAllInformationCrawler').then(function(res) {
				that.showLotteryInformationFlag = false;
				that.showInformationCrawlerFlag = true;
				that.informationCrawlers = res.body.data;
			});
		},

		backToInformationCrawlerPage : function() {
			this.showCollectionInformationFlag = false;
			this.toInformationCrawlerPage();
		},

		showAddSystemNoticeModal : function() {
			this.addOrUpdateSystemNoticeFlag = true;
			this.systemNoticeActionTitle = '新增公告';
			this.editSystemNotice = {
				noticeTitle : '',
				publishDate : '',
				noticeContent : ''
			};
			this.noticeContentEditor.txt.html('');
		},

		showEditSystemNoticeModal : function(id) {
			var that = this;
			that.$http.get('/systemNotice/findSystemNoticeById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.editSystemNotice = res.body.data;
				that.addOrUpdateSystemNoticeFlag = true;
				that.systemNoticeActionTitle = '编辑公告';
				this.noticeContentEditor.txt.html(that.editSystemNotice.noticeContent);
			});
		},

		addOrUpdateSystemNotice : function() {
			var that = this;
			var editSystemNotice = that.editSystemNotice;
			if (editSystemNotice.noticeTitle == null || editSystemNotice.noticeTitle == '') {
				layer.alert('请输入公告标题', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editSystemNotice.publishDate == null || editSystemNotice.publishDate == '') {
				layer.alert('请输入发布日期', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var noticeContent = this.noticeContentEditor.txt.html();
			if (noticeContent == null || noticeContent == '') {
				layer.alert('请输入公告内容', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			editSystemNotice.noticeContent = noticeContent;
			that.$http.post('/systemNotice/addOrUpdateSystemNotice', editSystemNotice).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addOrUpdateSystemNoticeFlag = false;
				that.refreshTable();
			});
		},

		delSystemNotice : function(id) {
			var that = this;
			layer.confirm('确定要删除吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/systemNotice/delSystemNoticeById', {
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
		},

		initTable : function() {
			var that = this;
			$('.lottery-information-table').bootstrapTable({
				classes : 'table table-hover',
				height : 490,
				url : '/lotteryInformation/findLotteryInformationByPage',
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
						source : that.source,
						title : that.title
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
					field : 'source',
					title : '来源'
				}, {
					field : 'title',
					title : '资讯标题'
				}, {
					field : 'createTime',
					title : '创建时间'
				}, {
					field : 'publishTime',
					title : '发布时间'
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						return [ '<button type="button" class="edit-system-notice-btn btn btn-outline-success btn-sm" style="margin-right: 4px;">编辑</button>', '<button type="button" class="del-system-notice-btn btn btn-outline-danger btn-sm">删除</button>' ].join('');
					},
					events : {
						'click .edit-system-notice-btn' : function(event, value, row, index) {
							that.showEditSystemNoticeModal(row.id);
						},
						'click .del-system-notice-btn' : function(event, value, row, index) {
							that.delSystemNotice(row.id);
						}
					}
				} ]
			});
		},

		refreshTable : function() {
			$('.lottery-information-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},
	}
});