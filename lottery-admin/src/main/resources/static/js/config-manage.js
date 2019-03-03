var configManageVM = new Vue({
	el : '#config-manage',
	data : {
		configCode : '',
		configName : '',

		addOrUpdateConfigFlag : false,
		configActionTitle : '',
		editConfig : {},

	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.initTable();
	},
	methods : {

		initTable : function() {
			var that = this;
			$('.config-manage-table').bootstrapTable({
				classes : 'table table-hover',
				height : 490,
				url : '/dictconfig/findConfigItemByPage',
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
						configCode : that.configCode,
						configName : that.configName
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
					field : 'configCode',
					title : '配置项code'
				}, {
					field : 'configName',
					title : '配置项名称'
				}, {
					field : 'configValue',
					title : '配置项值'
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						return [ '<button type="button" class="edit-config-btn btn btn-outline-success btn-sm" style="margin-right: 4px;">编辑</button>', '<button type="button" class="del-config-btn btn btn-outline-danger btn-sm">删除</button>' ].join('');
					},
					events : {
						'click .edit-config-btn' : function(event, value, row, index) {
							that.openEditConfigModal(row.id);
						},
						'click .del-config-btn' : function(event, value, row, index) {
							that.delConfig(row.id);
						}
					}
				} ]
			});
		},

		refreshTable : function() {
			$('.config-manage-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		openAddConfigModal : function() {
			this.addOrUpdateConfigFlag = true;
			this.configActionTitle = '新增配置项';
			this.editConfig = {
				configCode : '',
				configName : '',
				configValue : ''
			};
		},

		openEditConfigModal : function(id) {
			var that = this;
			that.$http.get('/dictconfig/findConfigItemById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.editConfig = res.body.data;
				that.addOrUpdateConfigFlag = true;
				that.configActionTitle = '编辑配置项';
			});
		},

		addOrUpdateConfig : function() {
			var that = this;
			var editConfig = that.editConfig;
			if (editConfig.configCode == null || editConfig.configCode == '') {
				layer.alert('请输入配置项code', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editConfig.configName == null || editConfig.configName == '') {
				layer.alert('请输入配置项名称', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editConfig.configValue == null || editConfig.configValue == '') {
				layer.alert('请输入配置项值', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/dictconfig/addOrUpdateConfig', editConfig).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addOrUpdateConfigFlag = false;
				that.refreshTable();
			});
		},

		delConfig : function(id) {
			var that = this;
			layer.confirm('确定要删除吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/dictconfig/delConfigById', {
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