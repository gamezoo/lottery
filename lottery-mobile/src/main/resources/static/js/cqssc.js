new Vue({
	el : '#content',
	data : {
		latelyIssue : {},
		currentIssue : {},
		bettingState : '',
		countdownHour : '00',
		countdownMinute : '00',
		countdownSecond : '00',

		openCountdownHour : '00',
		openCountdownMinute : '00',
		openCountdownSecond : '00',
		latelyThe5TimeLotteryResults : [],
		todayLatestBettingRecords : [],
		// 选中号码的注数
		bettingCount : 0,
		// 选中号码投注的金额
		bettingAmount : 0,
		// 投注金额底数
		baseAmount : 2,
		// 倍数
		multiple : 1,
		preBettingRecords : [],
		// 游戏玩法类别map
		gamePlayCategoryMap : new Map(),
		gamePlayCategorys : [],
		selectedGamePlayCategory : {},
		subGamePlayCategorys : [],
		selectedPlay : {},
		// 选号号位集合
		numLocates : []
	},
	computed : {
		// 监听倍数输入框的值,控制只能输入正整数数字
		inputMultiple : {
			get : function() {
				return this.multiple;
			},
			set : function(newValue) {
				if (/^[0-9]*[1-9][0-9]*$/.test(newValue)) {
					this.multiple = parseInt(newValue);
				} else if (newValue == '') {
					this.multiple = '';
				}
			}
		},

		// 预投注的总投注金额
		preBettingTotalAmount : {
			get : function() {
				var preBettingRecords = this.preBettingRecords;
				if (preBettingRecords.length == 0) {
					return 0;
				}
				var preBettingTotalAmount = 0;
				for (var i = 0; i < preBettingRecords.length; i++) {
					var preBettingRecord = preBettingRecords[i];
					preBettingTotalAmount += (this.baseAmount * this.multiple * preBettingRecord.bettingCount);
				}
				return preBettingTotalAmount;
			}
		},
		// 预投注的总注数
		preBettingTotalCount : {
			get : function() {
				var preBettingRecords = this.preBettingRecords;
				if (preBettingRecords.length == 0) {
					return 0;
				}
				var preBettingTotalCount = 0;
				for (var i = 0; i < preBettingRecords.length; i++) {
					preBettingTotalCount += preBettingRecords[i].bettingCount;
				}
				return preBettingTotalCount;
			}
		}
	},
	created : function() {
		this.refreshLatelyIssue();
		this.refreshCurrentIssue();
		this.loadGamePlay();
		this.refreshLatelyThe5TimeIssue();
		this.refreshTodayLatestBettingRecord();

	},
	methods : {
		showPlayIntroduceDesc : function() {
			layer.tips($('.play-introduce-desc').text(), '.play-introduce', {
				tips : [ 2, '#f13131' ],
				maxWidth : 250,
				time : 0,
				closeBtn : 0
			});
		},
		hidePlayIntroduceDesc : function() {
			layer.closeAll('tips');
		},

		/**
		 * 切换游戏玩法类别
		 */
		switchGamePlayCategory : function(gamePlayCategory) {
			var subGamePlayCategorys = [];
			var subGamePlayCategoryMap = this.gamePlayCategoryMap.get(gamePlayCategory.gamePlayCategoryName);
			for (key in subGamePlayCategoryMap) {
				var subGamePlayCategory = {
					subGamePlayCategoryName : key,
					plays : []
				};
				for (gamePlayKey in subGamePlayCategoryMap[key]) {
					subGamePlayCategory.plays.push(subGamePlayCategoryMap[key][gamePlayKey]);
				}
				subGamePlayCategorys.push(subGamePlayCategory);
			}
			this.selectedGamePlayCategory = gamePlayCategory;
			this.subGamePlayCategorys = subGamePlayCategorys;
			this.switchPlay(subGamePlayCategorys[0].plays[0]);
		},

		/**
		 * 切换玩法
		 */
		switchPlay : function(play) {
			this.selectedPlay = play;

			var numLocates = [];
			var numLocateArr = play.numLocates;
			for (var i = 0; i < numLocateArr.length; i++) {
				var data = numLocateArr[i];
				var numLocate = {
					locateName : data.numLocateName,
					maxSelected : data.maxSelected,
					hasFilterBtnFlag : data.hasFilterBtnFlag,
					nums : []
				};
				var numArr = data.nums.split(',');
				for (var j = 0; j < numArr.length; j++) {
					numLocate.nums.push({
						num : numArr[j],
						selectedFlag : false
					});
				}
				numLocates.push(numLocate);
			}
			this.numLocates = numLocates;
			this.resetBettingCountAndAmount();
		},

		/**
		 * 加载游戏玩法
		 */
		loadGamePlay : function() {
			var that = this;
			this.$http.get('/game/findGamePlayAndNumLocateByGameCode', {
				params : {
					gameCode : 'CQSSC'
				}
			}).then(function(res) {
				var gamePlayCategorys = [];
				var gamePlayCategoryMap = new Map();
				var result = res.body.data;
				for (var i = 0; i < result.length; i++) {
					var obj = result[i];
					if (obj.state == '0') {
						continue;
					}
					var gamePlay = {
						gamePlayCode : obj.gamePlayCode,
						gamePlayName : obj.gamePlayName,
						gamePlayDesc : obj.gamePlayDesc,
						odds : obj.odds,
						numLocates : obj.numLocates,
						gameCode : obj.gameCode,
						gamePlayCategoryCode : obj.gamePlayCategoryCode,
						gamePlayCategoryName : obj.gamePlayCategoryName,
						subGamePlayCategoryCode : obj.subGamePlayCategoryCode,
						subGamePlayCategoryName : obj.subGamePlayCategoryName
					};
					var gamePlayCategoryName = gamePlay.gamePlayCategoryName;
					var subGamePlayCategoryName = gamePlay.subGamePlayCategoryName;
					if (gamePlayCategoryMap.get(gamePlayCategoryName) == null) {
						gamePlayCategoryMap.set(gamePlayCategoryName, {});
						gamePlayCategorys.push({
							gamePlayCategoryCode : gamePlay.gamePlayCategoryCode,
							gamePlayCategoryName : gamePlayCategoryName
						});
					}
					var gamePlayCategory = gamePlayCategoryMap.get(gamePlayCategoryName);
					if (gamePlayCategory[subGamePlayCategoryName] == null) {
						gamePlayCategory[subGamePlayCategoryName] = {};
					}
					gamePlayCategory[subGamePlayCategoryName][gamePlay.gamePlayName] = gamePlay;
				}

				that.gamePlayCategorys = gamePlayCategorys;
				that.gamePlayCategoryMap = gamePlayCategoryMap;
				that.switchGamePlayCategory(gamePlayCategorys[0]);
			});
		},

		/**
		 * 全选号球
		 */
		selectAllNo : function(index) {
			var nums = this.numLocates[index].nums;
			for (var i = 0; i < nums.length; i++) {
				nums[i].selectedFlag = true;
			}
			this.calcBettingCountAndAmount();
		},

		/**
		 * 选择全部奇数号球
		 */
		selectOddNum : function(index) {
			var nums = this.numLocates[index].nums;
			for (var i = 0; i < nums.length; i++) {
				nums[i].selectedFlag = !(nums[i].num % 2 == 0);
			}
			this.calcBettingCountAndAmount();
		},

		/**
		 * 选择全部偶数号球
		 */
		selectEvenNum : function(index) {
			var nums = this.numLocates[index].nums;
			for (var i = 0; i < nums.length; i++) {
				nums[i].selectedFlag = (nums[i].num % 2 == 0);
			}
			this.calcBettingCountAndAmount();
		},

		/**
		 * 选择全部大的号球
		 */
		selectBigNum : function(index) {
			var nums = this.numLocates[index].nums;
			var bigStartIndex = parseInt(nums.length / 2);
			for (var i = 0; i < nums.length; i++) {
				nums[i].selectedFlag = i >= bigStartIndex;
			}
			this.calcBettingCountAndAmount();
		},

		/**
		 * 选择全部小的号球
		 */
		selectSmallNum : function(index) {
			var nums = this.numLocates[index].nums;
			var smallEndIndex = parseInt(nums.length / 2) - 1;
			for (var i = 0; i < nums.length; i++) {
				nums[i].selectedFlag = i <= smallEndIndex;
			}
			this.calcBettingCountAndAmount();
		},

		/**
		 * 清空所有位置号球选中状态
		 */
		clearAllLocateSelected : function() {
			for (var i = 0; i < this.numLocates.length; i++) {
				var nums = this.numLocates[i].nums;
				for (var j = 0; j < nums.length; j++) {
					nums[j].selectedFlag = false;
				}
			}
		},

		/**
		 * 清空号球选中状态
		 */
		clearSelected : function(index) {
			var nums = this.numLocates[index].nums;
			for (var i = 0; i < nums.length; i++) {
				nums[i].selectedFlag = false;
			}
			this.calcBettingCountAndAmount();
		},

		/**
		 * 切换号球选中状态
		 */
		switchNumSelectedState : function(i, j) {
			var selectedFlag = this.numLocates[i].nums[j].selectedFlag;
			this.numLocates[i].nums[j].selectedFlag = !selectedFlag;
			this.calcBettingCountAndAmount();
		},

		/**
		 * 获取选择的号球
		 */
		getSelectedBalls : function() {
			var selectedBalls = [];
			for (var i = 0; i < this.numLocates.length; i++) {
				var arr = [];
				var nums = this.numLocates[i].nums;
				for (var j = 0; j < nums.length; j++) {
					var num = nums[j];
					if (num.selectedFlag) {
						arr.push(num.num);
					}
				}
				selectedBalls.push(arr);
			}
			return selectedBalls;
		},

		/**
		 * 切换元角模式
		 */
		switchYuanJiaoMode : function(baseAmount) {
			var that = this;
			if (that.preBettingRecords.length > 0) {
				layer.confirm('切换元角分模式将影响您现有投注项,是否继续?', {
					icon : 7,
					title : '提示'
				}, function(index) {
					var preBettingRecords = that.preBettingRecords;
					for (var i = 0; i < preBettingRecords.length; i++) {
						preBettingRecords[i].bettingAmount = baseAmount;
					}
					that.baseAmount = baseAmount;
					layer.close(index);
				});
			} else {
				that.baseAmount = baseAmount;
			}
		},

		/**
		 * 倍数减1,倍数最低是1倍
		 */
		multipleSubtract1 : function() {
			if (!/^[0-9]*[1-9][0-9]*$/.test(this.multiple)) {
				this.multiple = 1;
			}
			if (this.multiple > 1) {
				this.multiple = this.multiple - 1;
			}
		},

		/**
		 * 倍数加1
		 */
		multipleAdd1 : function() {
			if (!/^[0-9]*[1-9][0-9]*$/.test(this.multiple)) {
				this.multiple = 1;
			}
			this.multiple = this.multiple + 1;
		},

		/**
		 * 重置选中号码的注数和投注的金额
		 */
		resetBettingCountAndAmount : function() {
			this.bettingCount = 0;
			this.bettingAmount = 0;
		},

		/**
		 * 计算选中号码的注数和投注的金额
		 */
		calcBettingCountAndAmount : function() {
			var selectedBalls = this.getSelectedBalls();
			var ballJoins = [];
			for (var i = 0; i < selectedBalls.length; i++) {
				ballJoins.push(selectedBalls[i].join(''));
			}
			var bettingCount = 0;
			var packageNumGroup = [];
			switch (this.selectedPlay.gamePlayCode) {
			case '5XDW':
				for (var c = 0; c < 5; c++) {
					bettingCount += ballJoins[c].length;
				}
				break;
			case 'H1ZX':
				bettingCount = ballJoins[ballJoins.length - 1].length;
				break;
			case '5XZX':
				bettingCount = ballJoins[0].length * ballJoins[1].length * ballJoins[2].length * ballJoins[3].length * ballJoins[4].length;
				break;
			case 'Q4ZX':
			case 'H4ZX':
				bettingCount = ballJoins[0].length * ballJoins[1].length * ballJoins[2].length * ballJoins[3].length;
				break;
			case 'Q3ZX':
			case 'Z3ZX':
			case 'H3ZX':
				bettingCount = ballJoins[0].length * ballJoins[1].length * ballJoins[2].length;
				break;
			case 'Q3ZXHZ':
				bettingCount = 0;
				break;
			case 'Q2ZX':
			case 'H2ZX':
				bettingCount = ballJoins[0].length * ballJoins[1].length;
				break;
			case 'Q31M':
			case 'Z31M':
			case 'H31M':
			case '4X1M':
				bettingCount = ballJoins[0].length;
				break;
			case 'Q2ZUX':
			case 'H2ZUX':
			case 'Q32M':
			case 'Z32M':
			case 'H32M':
			case '4X2M':
			case '5X2M':
				bettingCount = ballJoins[0].length * (ballJoins[0].length - 1) / 2;
				break;
			case '5X3M':
				bettingCount = ballJoins[0].length * (ballJoins[0].length - 1) * (ballJoins[0].length - 2) / 6;
				break;
			case 'R2ZX':
				for (var c = 0; c < 4; c++) {
					for (var p = c + 1; p < 5; p++) {
						bettingCount += ballJoins[c].length * ballJoins[p].length;
					}
				}
				break;
			default:
				throw 'unknown play ' + this.selectedPlayCode
			}
			this.bettingCount = bettingCount;
			this.bettingAmount = bettingCount * this.baseAmount;
		},

		generatePreBettingRecords : function() {
			if (this.bettingCount == 0) {
				return;
			}
			var selectedNos = [];
			var selectedBalls = this.getSelectedBalls();
			for (var i = 0; i < selectedBalls.length; i++) {
				if (selectedBalls[i].length == 0) {
					selectedNos.push('-');
				} else {
					selectedNos.push(selectedBalls[i].join(''));
				}
			}
			var preBettingRecords = [ {
				gamePlayCategoryCode : this.selectedPlay.gamePlayCategoryCode,
				gamePlayCode : this.selectedPlay.gamePlayCode,
				gamePlayName : this.selectedPlay.gamePlayName,
				selectedNo : selectedNos.join(','),
				bettingCount : this.bettingCount
			} ];
			return preBettingRecords;
		},

		/**
		 * 添加到预选投注记录列表
		 */
		addToPreBettingOrder : function() {
			var preBettingRecords = this.generatePreBettingRecords();
			if (preBettingRecords.length == 0) {
				return;
			}
			this.preBettingRecords = this.preBettingRecords.concat(preBettingRecords);
			this.clearAllLocateSelected();
			this.resetBettingCountAndAmount();
		},

		/**
		 * 删除预选投注记录
		 */
		delPreBettingOrder : function(index) {
			this.preBettingRecords.splice(index, 1);
		},

		/**
		 * 清空预选投注记录
		 */
		clearPreBettingOrder : function() {
			this.preBettingRecords = [];
		},

		/**
		 * 下单
		 */
		placeOrder : function() {
			var that = this;
			var placeOrderParam = {
				gameCode : that.selectedPlay.gameCode,
				issueNum : that.currentIssue.issueNum,
				baseAmount : that.baseAmount,
				multiple : that.multiple,
				bettingRecords : that.preBettingRecords
			};
			that.$http.post('/betting/placeOrder', placeOrderParam).then(function(res) {
				layer.alert('下单成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.clearPreBettingOrder();
				header.refreshBalance();
				that.refreshTodayLatestBettingRecord();
			});
		},

		/**
		 * 刷新今日最新的投注记录
		 */
		refreshTodayLatestBettingRecord : function() {
			var that = this;
			that.$http.get('/betting/findTodayLatestThe5TimeBettingRecord', {
				params : {
					gameCode : 'CQSSC'
				}
			}).then(function(res) {
				var todayLatestBettingRecords = [];
				for (var i = 0; i < res.body.data.length; i++) {
					var bettingRecord = {
						bettingOrderId : res.body.data[i].bettingOrderId,
						gamePlayName : res.body.data[i].gamePlayName,
						issueNum : res.body.data[i].issueNum,
						bettingAmount : res.body.data[i].bettingAmount,
						stateName : res.body.data[i].stateName
					};
					todayLatestBettingRecords.push(bettingRecord);
				}
				that.todayLatestBettingRecords = todayLatestBettingRecords;
			});
		},

		splitlotteryNum : function(lotteryNum) {
			if (lotteryNum == null || lotteryNum == '') {
				return [];
			}
			return lotteryNum.split(',');
		},

		getNextIssue : function() {
			var that = this;
			that.$http.get('/issue/getNextIssue', {
				params : {
					gameCode : 'CQSSC'
				}
			}).then(function(res) {
				var currentTime = dayjs(res.body.timestamp);
				var startTime = dayjs(res.body.data.startTime);
				var residueSecond = startTime.diff(currentTime, 'second');
				that.residueSecond = residueSecond;
				that.openCountdown();
			});
		},

		/**
		 * 开市倒计时
		 */
		openCountdown : function() {
			var that = this;
			var openCountdownInterval = window.setInterval(function() {
				var residueSecond = that.residueSecond;
				that.updateOpenCountdownClock(residueSecond);

				residueSecond--;
				that.residueSecond = residueSecond;

				if (residueSecond < 0) {
					window.clearInterval(openCountdownInterval);
					that.refreshLatelyIssue();
					that.refreshCurrentIssue();
				}
			}, 1000);
		},

		/**
		 * 更新开市倒计时
		 */
		updateOpenCountdownClock : function(residueSecond) {
			var that = this;
			var openCountdownHour = 0;
			var openCountdownMinute = 0;
			var openCountdownSecond = 0;
			if (residueSecond > 0) {
				openCountdownHour = parseInt(residueSecond / (60 * 60) % 24);
				openCountdownMinute = parseInt(residueSecond / 60 % 60);
				openCountdownSecond = parseInt(residueSecond % 60);
			}
			if (openCountdownHour < 10) {
				openCountdownHour = '0' + openCountdownHour;
			}
			if (openCountdownMinute < 10) {
				openCountdownMinute = '0' + openCountdownMinute;
			}
			if (openCountdownSecond < 10) {
				openCountdownSecond = '0' + openCountdownSecond;
			}
			that.openCountdownHour = openCountdownHour;
			that.openCountdownMinute = openCountdownMinute;
			that.openCountdownSecond = openCountdownSecond;
		},

		/**
		 * 刷新最近的期号
		 */
		refreshLatelyIssue : function(updateFlag) {
			var that = this;
			that.$http.get('/issue/getLatelyIssue', {
				params : {
					gameCode : 'CQSSC'
				}
			}).then(function(res) {
				var latelyIssue = res.body.data;
				if (latelyIssue == null) {
					that.latelyIssue = {
						issue : '**'
					};
					return;
				}
				that.latelyIssue = latelyIssue;
				var lotteryNumArr = that.splitlotteryNum(that.latelyIssue.lotteryNum);
				if (lotteryNumArr == 0) {
					window.setTimeout(function() {
						that.refreshLatelyIssue(true);
					}, 5000);
				} else if (updateFlag) {
					header.refreshBalance();
					that.refreshLatelyThe5TimeIssue();
					that.refreshTodayLatestBettingRecord();
				}
			});
		},

		/**
		 * 更新倒计时
		 */
		updateCountdownClock : function(residueSecond) {
			var that = this;
			var countdownHour = 0;
			var countdownMinute = 0;
			var countdownSecond = 0;
			if (residueSecond > 0) {
				countdownHour = parseInt(residueSecond / (60 * 60) % 24);
				countdownMinute = parseInt(residueSecond / 60 % 60);
				countdownSecond = parseInt(residueSecond % 60);
			}
			if (countdownHour < 10) {
				countdownHour = '0' + countdownHour;
			}
			if (countdownMinute < 10) {
				countdownMinute = '0' + countdownMinute;
			}
			if (countdownSecond < 10) {
				countdownSecond = '0' + countdownSecond;
			}
			that.countdownHour = countdownHour;
			that.countdownMinute = countdownMinute;
			that.countdownSecond = countdownSecond;
		},

		/**
		 * 开奖倒计时,15秒时间 已截止投注
		 */
		lotteryCountdown : function() {
			var that = this;
			var lotteryCountdownInterval = window.setInterval(function() {
				var residueSecond = that.residueSecond;
				that.updateCountdownClock(residueSecond);

				residueSecond--;
				that.residueSecond = residueSecond;
				that.bettingState = '已截止投注';

				if (residueSecond < 0) {
					window.clearInterval(lotteryCountdownInterval);
					that.refreshLatelyIssue(true);
					that.refreshCurrentIssue();
				}
			}, 1000);
		},

		/**
		 * 投注倒计时,9分30秒时间 可以投注
		 */
		bettingCountdown : function() {
			var that = this;
			var bettingCountdownInterval = window.setInterval(function() {
				var residueSecond = that.residueSecond;
				that.updateCountdownClock(residueSecond);

				residueSecond--;
				that.residueSecond = residueSecond;
				that.bettingState = '可以投注';

				if (residueSecond < 0) {
					window.clearInterval(bettingCountdownInterval);
					layer.alert(that.currentIssue.issue + '期销售已封盘....,请进入下一期购买', {
						icon : 7,
						time : 3000,
						shade : false
					});
					that.residueSecond = 15;
					that.lotteryCountdown();
				}
			}, 1000);
		},

		/**
		 * 刷新当前的期号
		 */
		refreshCurrentIssue : function() {
			var that = this;
			that.$http.get('/issue/getCurrentIssue', {
				params : {
					gameCode : 'CQSSC'
				}
			}).then(function(res) {
				var currentIssue = res.body.data;
				if (currentIssue == null) {
					that.currentIssue = {
						issue : '*************'
					};
					that.getNextIssue();
					return;
				}
				that.currentIssue = currentIssue;
				var currentTime = dayjs(res.body.timestamp);
				var endTime = dayjs(res.body.data.endTime);
				var residueSecond = endTime.diff(currentTime, 'second');
				if (residueSecond > 15) {
					that.residueSecond = residueSecond - 15;
					that.bettingCountdown();
				} else if (residueSecond > 0 && residueSecond <= 15) {
					that.residueSecond = residueSecond;
					that.lotteryCountdown();
				}
			});
		},

		/**
		 * 刷新最近5次的开奖期号数据
		 */
		refreshLatelyThe5TimeIssue : function() {
			this.$http.get('/issue/findLatelyThe5TimeIssue', {
				params : {
					gameCode : 'CQSSC'
				}
			}).then(function(res) {
				var latelyThe5TimeLotteryResults = [];
				for (var i = 0; i < res.body.data.length; i++) {
					var lotteryNum = res.body.data[i].lotteryNum;
					var lotteryResult = {
						issueNum : res.body.data[i].issueNum,
						lotteryNums : lotteryNum != null ? lotteryNum.split(",") : []
					};
					latelyThe5TimeLotteryResults.push(lotteryResult);
				}
				this.latelyThe5TimeLotteryResults = latelyThe5TimeLotteryResults;
			});
		},

		gotoBettingRecordPage : function() {
			window.location.href = '/betting-record';
		}
	}
});