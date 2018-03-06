// 处理任务
var gulp = require("gulp");
var imagemin = require("gulp-imagemin");
var uglify = require("gulp-uglify");
var sass = require('gulp-sass');
var concat = require('gulp-concat');

// 常用方法
// gulp.task  -- 定义任务
// gulp.src -- 找到需要执行任务的文件
// gulp.dest -- 执行任务的文件的去处
// gulp.watch -- 观察文件是否发生变化

// 定义任务
// 执行任务 gulp message
gulp.task("message", function(){
	return console.log("Gulp is running.");
});

// 拷贝文件
gulp.task("copyHtmls", function(){
	gulp.src("src/*.html")
		.pipe(gulp.dest("dist"));
});

// 图片压缩
gulp.task("imageMin", function(){
	gulp.src("src/images/*")
		.pipe(imagemin())
		.pipe(gulp.dest("dist/images"));
});

// 压缩js文件
gulp.task("minify", function(){
	gulp.src("src/js/*.js")
		.pipe(uglify())
		.pipe(gulp.dest("dist/js"));
});

// Sass转换css
gulp.task("sass", function(){
	gulp.src("src/sass/*.scss")
		.pipe(sass().on("error", sass.logError))
		.pipe(gulp.dest("dist/css"));
});

// 合并代码
gulp.task("scripts", function(){
	gulp.src("src/js/*.js")
		.pipe(concat("all.js"))
		.pipe(uglify())
		.pipe(gulp.dest("dist/js"));
});

// 监听文件是否发生变化
gulp.task("watch", function(){
	gulp.watch("src/js/*.js", ['scripts']);
	gulp.watch("src/images/*", ['imageMin']);
	gulp.watch("src/sass/*.scss", ['sass']);
	gulp.watch("src/*.html", ['copyHtmls']);
});

// 执行多任务
// 定义默认任务
gulp.task("default", ["message", "copyHtmls", "imageMin", "sass", "scripts"]);