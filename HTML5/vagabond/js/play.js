var playState = {
    gameOver: true,
    isJumping: false,
    gameTitleText: "VAGABOND",
    gameoverText: "GAME OVER",

    levels: [{ name: "Level1", map: [0,0,0,0,0,0,1,0,0,0,1,1,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1]},
        { name: "Level2", map: [0,0,0,0,1,0,1,0,0,1,0,1,0,0,1,0,1,0,1,0,1,0,1,1,1,0,1,0,1,1]}
    ],
    create: function () {
        // sky
        var sky = game.add.image(0, 0, 'sky');
        sky.fixedToCamera = true;

        // rotating world
        this.rotWorldGrp = game.add.group();
        var worldSprite = this.rotWorldGrp.create(0,0, 'world1');
        // 1.005 makes the world little bit bigger.
        var scaling_factor = game.height * 1.05 / worldSprite.height;
        worldSprite.scale.x = scaling_factor;
        worldSprite.scale.y = scaling_factor;
        worldSprite.anchor.setTo(0.5, 0.5);
        this.worldRadius = worldSprite.width * scaling_factor /2;
        this.rotWorldGrp.x = game.width / 2;
        this.rotWorldGrp.y = game.height;

        // player
        this.player = game.add.sprite(game.width/2, game.height/2, 'mummy');
        this.player.anchor.setTo(0.5, 1.0);
        this.player.animations.add('walk');
        this.player.animations.play('walk', 12, true);
        game.physics.enable(this.player, Phaser.Physics.ARCADE);

        // obstacles
        this.obs = game.add.group();
        this.obs.createMultiple(30, "stone");
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
        this.cameraScene.scale.x = 0.5;
        this.cameraScene.scale.y = 0.5;

        // input
        //this.space_key = game.input.keyboard.addKey(Phaser.Keyboard.SPACEBAR);
        //this.space_key.onDown.add(this.jump, this);
    },

    update: function() {
        if (game.input.activePointer.isDown)
        {
            if (playState.gameOver === false)
                playState.jump();
            else
                playState.startGame();
        }
        if (this.gameOver === false && this.cameraScene.scale.x === 1){
            var rotationSpeed = 0.8;
            if (playState.isJumping === true) {
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
            if (map[i] === 1)
                this.placeObstacles(i * rad);
        }
    },

    placeObstacles: function(angle) {
        var obstacle = this.obs.getFirstDead();
        if (obstacle == null)
            return;

        var ro = this.worldRadius * Math.PI;
        var x = ro * Math.sin(angle);
        var y = ro * Math.cos(angle);
        obstacle.anchor.setTo(0.5,0.5);
        // y grows going down
        obstacle.reset(x, -y);
        obstacle.rotation = angle;
        game.physics.enable(obstacle, Phaser.Physics.ARCADE);
        this.rotWorldGrp.add(obstacle);
    },

    hit: function() {
        playState.obs.forEachAlive(function(r){r.kill();}, this);
        playState.setGameOver();
    },

    setGameOver: function() {
        playState.gameOver = true;
        var zoomOutTween = game.add.tween(this.cameraScene.scale).to({ x: 0.5, y: 0.5});
        //zoomOutTween._lastChild.onComplete.add(function(){playState.isJumping = false;}, this);
        zoomOutTween.start();
    },

    jump: function() {
        if (playState.gameOver === true){
            this.playAgain();
            return;
        }
        if (playState.isJumping === true){
            return;
        }
        var playerInitialY = this.player.y;

        playState.isJumping = true;
        var jumpTween = game.add.tween(playState.player);
        jumpTween.to({ y: playerInitialY - 70 }, 100);
        jumpTween.to({ y: playerInitialY}, 400);
        jumpTween._lastChild.onComplete.add(function(){playState.isJumping = false;}, this);
        jumpTween.start();
    },

    startGame: function() {
        this.draw_level();
        this.rotWorldGrp.rotation = 0;
        this.gameOver = false;
        var zoomInTween = game.add.tween(this.cameraScene.scale).to({ x: 1, y: 1});
        //zoomOutTween._lastChild.onComplete.add(function(){playState.isJumping = false;}, this);
        zoomInTween.start();
    },

    nextLevel: function() {
        this.draw_level();
        this.rotWorldGrp.rotation = 0;
    }
}