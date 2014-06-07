var playState = {
    // Vars
    gameOver: true,
    isJumping: 0, // Jumping states 0=no jump, 1=first_tween, 2=first_desend, 3= second_jump,
    allowedJumps: 3,
    zoomOutLevel: 0.5,
    zoomInLevel: 1.3,
    atmosphereWorldRatio: 1.5,
    playerScalingFactor: 0.25,
    currentLevel: 0,

    // Costants
    rationPlayerInitYJumpY:0.7,
    GAMETITLE_TEXT: "VAGABOND",
    GAMEOVER_TEXT: "GAME OVER",
    RAD: 2 *Math.PI /30,
    LEVELS: [
        //{ name: "Level0", map: [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]},
        //{ name: "TEST", map: [1,2,3,4,5,0,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29]},
        { name: "WORLD 1-1", map: [0,0,0,0,0,0,5,7,0,0,9,10,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1]},
        { name: "WORLD 2", map: [0,0,0,0,1,0,1,0,0,1,0,1,0,0,1,0,1,0,1,0,1,0,1,1,1,0,1,0,1,1]}
    ],

    // Functions
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
        playState.totPlayerScalingFactor = scaling_factor * this.playerScalingFactor;
        this.player.alpha = 0;

        //this.player.scale.x = totPlayerScalingFactor;
        //this.player.scale.y = totPlayerScalingFactor;

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
        this.obs.setAll("alive", false);

        this.draw_level();

        // Writings
        var fontSize = 128;
        this.bigWriting = game.add.bitmapText(game.width, game.height - 100, 'fonts', this.LEVELS[this.currentLevel].name, fontSize);

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

        //audio
        playState.JUMP_AUDIO = game.add.audio('jump_fx');
        playState.CRASH_AUDIO = game.add.audio('crash_fx');

        // animations
        playState.EMITTER = game.add.emitter(0, 0, 100);
        playState.EMITTER.makeParticles('crash_particle');
    },

    update: function() {
        if (this.gameOver === false && this.cameraScene.scale.x === playState.zoomInLevel){
            var rotationSpeed = 0.8;
            if (playState.isJumping > 0) {
                rotationSpeed = 1.2;
            }

            if (Math.abs(this.rotWorldGrp.angle) > 360)
                this.rotWorldGrp.angle = 0;
            this.rotWorldGrp.angle -= rotationSpeed;
            game.physics.arcade.overlap( this.rotWorldGrp,this.player, this.hit);
        }
    },

    render: function() {},

    // Extras
    draw_level: function() {
        console.log("Drawing level...");
        var level = this.LEVELS[playState.currentLevel];
        var map = level.map;
        for (var i=0; i < map.length; ++i){
            var obstacle_type= map[i] -1;
            if (obstacle_type > -1){
                var angle = i * playState.RAD;
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
        obstacle.type = obstacle_type;
        // y grows going down
        obstacle.reset(x, -y);
        obstacle.anchor.setTo(0.5,1);
        obstacle.scale.y = 0;
        obstacle.rotation = angle;
        obstacle.alive = true;

        game.physics.enable(obstacle, Phaser.Physics.ARCADE);
        this.rotWorldGrp.add(obstacle);

        // add anim
        var placeObsTween = game.add.tween(obstacle.scale);
        placeObsTween.to({ y: obstacle.scale.x }, 1000);
        placeObsTween.start();
        console.log(obstacle);
    },

    hit: function(player, obs) {

        // Obstacle logic
        switch (obs.type){
            case 1:
                playState.spaceshipDamaged();
            default:
                // normal obstacle.
                playState.spaceshipCrash();
                playState.setGameOver();
        }
    },

    spaceshipDamaged: function() {
        // player feedback when hit!
        var hitPlayerTween = game.add.tween(player);
        hitPlayerTween.to({ alpha: 4}, 100);
        hitPlayerTween.to({ alpha: 1}, 100);
        hitPlayerTween.to({ alpha: 4}, 100);
        hitPlayerTween.to({ alpha: 1}, 50);
        hitPlayerTween.start();
    },

    spaceshipCrash: function() {
        var explodeEffect = true;
        var particleLifeSpan = 2000;
        var particleNumber = 10;

        playState.CRASH_AUDIO.play();
        playState.EMITTER.x = playState.player.x;
        playState.EMITTER.y = playState.player.y;
        playState.player.alpha = 0;

        playState.EMITTER.start(explodeEffect, particleLifeSpan, null, particleNumber);
    },
    spaceshipNew: function() {
        playState.player.alpha = 1;
        playState.player.scale.x = 1;
        playState.player.scale.y = 1;

        var newPlayerTween = game.add.tween(playState.player.scale);
        newPlayerTween.to({ x: playState.totPlayerScalingFactor, y: playState.totPlayerScalingFactor}, 1000,  Phaser.Easing.Bounce.Out);
        newPlayerTween.start();
    },

    setGameOver: function() {
        playState.obs.setAll("alive", false);
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
        jumpTween.to({ y: playState.playerInitialY * playState.rationPlayerInitYJumpY }, 100);
        jumpTween.onComplete.add(function(){playState.isJumping += 1;}, this);
        jumpTween.to({ y: playState.playerInitialY}, 300);
        jumpTween._lastChild.onComplete.add(function(){

        }, this);
        jumpTween.start();
        playState.JUMP_AUDIO.play();
    },

    startGame: function() {
        playState.spaceshipNew();

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