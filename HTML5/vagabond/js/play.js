var playState = {
    gameOver: true,
    isJumping: 0, // Jumping states 0=no jump, 1=first_tween, 2=first_desend, 3= second_jump,
    gameTitleText: "VAGABOND",
    gameoverText: "GAME OVER",
    allowedJumps: 3,
    zoomOutLevel: 0.5,
    zoomInLevel: 1.3,
    atmosphereWorldRatio: 1.5,
    playerScalingFactor: 0.30,

    levels: [
        //{ name: "Level0", map: [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]},
        { name: "Level1", map: [0,0,0,0,0,0,1,2,0,0,1,1,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1]},
        { name: "Level2", map: [0,0,0,0,1,0,1,0,0,1,0,1,0,0,1,0,1,0,1,0,1,0,1,1,1,0,1,0,1,1]}
    ],
    create: function () {
        adaptToScreen(this);
        // sky
        //var sky = game.add.image(0, 0, 'sky');
        //sky.fixedToCamera = true;

        // rotating world
        this.rotWorldGrp = game.add.group();
        var worldSprite = this.rotWorldGrp.create(0,0, 'world1');
        //this.worldRadius = worldSprite.width/2;// * scaling_factor /2;
        var scaling_factor = game.height * 1.05 / worldSprite.height * this.atmosphereWorldRatio;
        worldSprite.scale.x = scaling_factor;
        worldSprite.scale.y = scaling_factor ;
        worldSprite.anchor.setTo(0.5, 0.5);

        this.rotWorldGrp.x = game.width / 2;
        this.rotWorldGrp.y = game.height;

        // player
        this.player = game.add.sprite(game.width/2, game.height/2, 'navicella');
        var totPlayerScalingFactor = scaling_factor * this.playerScalingFactor;
        this.player.scale.x = totPlayerScalingFactor;
        this.player.scale.y = totPlayerScalingFactor;

        this.player.anchor.setTo(0.5, 1.0);
        //this.player.animations.add('walk');
        //this.player.animations.play('walk', 12, true);
        this.playerInitialY = this.player.y;
        game.physics.enable(this.player, Phaser.Physics.ARCADE);

        // obstacles
        this.obs = game.add.group();
        this.obs.createMultiple(30, "obstacles");
        this.obs.setAll("scale.x", scaling_factor);
        this.obs.setAll("scale.y", scaling_factor);
        this.draw_level();

        // Writings
        var fontSize = 128;
        this.bigWriting = game.add.bitmapText(game.width, game.height - 100, 'fonts', this.gameTitleText, fontSize);

        // camera scene
        this.cameraScene = game.add.group();
        this.cameraScene.add(this.rotWorldGrp);
        this.cameraScene.add(this.player);
        this.cameraScene.add(this.bigWriting);
        this.cameraScene.scale.x = playState.zoomOutLevel;
        this.cameraScene.scale.y = playState.zoomOutLevel;

        // input
        game.input.onDown.add(function() {
            if (playState.gameOver === false)
                playState.jump();
            else
                playState.startGame();
        }, this);
    },

    update: function() {
        if (this.gameOver === false && this.cameraScene.scale.x === playState.zoomInLevel){
            var rotationSpeed = 0.8;
            if (playState.isJumping > 0) {
                rotationSpeed = 1.2;
            }

            this.rotWorldGrp.angle -= rotationSpeed;
            game.physics.arcade.overlap(this.player, this.rotWorldGrp, this.hit);
        }
    },

    render: function() {},

    // Extras
    draw_level: function() {
        var rad = 2 *Math.PI /30;

        var level = this.levels[0];
        var map = level.map;
        for (var i=0; i < map.length; ++i){
            var obstacle_type= map[i] -1;
            if (obstacle_type > -1){
                var angle = i * rad;
                this.placeObstacles(angle, obstacle_type);
            }
        }
    },

    placeObstacles: function(angle, obstacle_type) {
        var obstacle = this.obs.getFirstDead();
        if (obstacle == null)
            return;

        var ro = this.playerInitialY * 0.95;
        var x = ro * Math.sin(angle);
        var y = ro * Math.cos(angle);
        obstacle.anchor.setTo(0.5,0.5);
        obstacle.frame = obstacle_type;
        // y grows going down
        obstacle.reset(x, -y);
        obstacle.anchor.setTo(0.5,1);
        obstacle.scale.y = 0;
        obstacle.rotation = angle;
        game.physics.enable(obstacle, Phaser.Physics.ARCADE);
        this.rotWorldGrp.add(obstacle);

        // add anim
        var placeObsTween = game.add.tween(obstacle.scale);
        placeObsTween.to({ y: obstacle.scale.x }, 1000);
        placeObsTween.start();
    },

    hit: function() {
        /*playState.obs.forEach(function(r){
            var killObsTween = game.add.tween(r.scale);
            killObsTween.to({ y: 0.1 }, 1000);
            //killObsTween.onComplete.add(function(){r.kill()});
            killObsTween.start();
        }, this);*/
        playState.setGameOver();
    },

    setGameOver: function() {
        playState.gameOver = true;
        var zoomOutTween = game.add.tween(this.cameraScene.scale).to({ x: playState.zoomOutLevel, y: playState.zoomOutLevel});
        zoomOutTween.start();
    },

    jump: function() {
        if (playState.gameOver === true){
            this.playAgain();
            return;
        }
        if (playState.player.y === playState.playerInitialY){
            playState.isJumping = 0;
        }

        if (  playState.isJumping >= playState.allowedJumps){
            return;
        }

        var jumpTween = game.add.tween(playState.player);
        var rationPlayerInitYJumpY = .80;
        jumpTween.to({ y: playState.playerInitialY * rationPlayerInitYJumpY }, 100);
        jumpTween.onComplete.add(function(){playState.isJumping += 1;}, this);
        jumpTween.to({ y: playState.playerInitialY}, 400);
        jumpTween._lastChild.onComplete.add(function(){

        }, this);
        jumpTween.start();
    },

    startGame: function() {
        this.draw_level();
        this.rotWorldGrp.rotation = 0;
        this.gameOver = false;
        var zoomInTween = game.add.tween(this.cameraScene.scale).to({ x: playState.zoomInLevel, y: playState.zoomInLevel});
        zoomInTween.start();
    },

    nextLevel: function() {
        this.draw_level();
        this.rotWorldGrp.rotation = 0;
    }
}