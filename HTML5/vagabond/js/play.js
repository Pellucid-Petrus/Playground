var playState = {
    gameOver: false,
    isJumping: false,

    create: function () {
        // sky
        var sky = game.add.image(0, 0, 'sky');
        //sky.fixedToCamera = true;

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
        this.player.animations.play('walk', 14, true);
        game.physics.enable(this.player, Phaser.Physics.ARCADE);

        // obstacles
        this.obs = game.add.group();
        this.obs.createMultiple(10, "stone");
        this.obs.setAll("scale.x", scaling_factor);
        this.obs.setAll("scale.y", scaling_factor);
        this.draw_level();

        // input
        this.space_key = game.input.keyboard.addKey(Phaser.Keyboard.SPACEBAR);
		this.space_key.onDown.add(this.jump, this);
    },

    update: function() {
        if (this.gameOver == false){
            var rotationSpeed = 0.8;
            if (playState.isJumping === true) {
                rotationSpeed = 1.0;
            }

            this.rotWorldGrp.angle -= rotationSpeed;
            game.physics.arcade.overlap(this.player, this.rotWorldGrp, this.hit);
        }
    },

    render: function() {},

    // Extras
    draw_level: function() {
        var rad = 2 *Math.PI /12;
        this.placeObstacles(0);
        this.placeObstacles(3 * rad);
        this.placeObstacles(6 * rad);
        this.placeObstacles(9 * rad);
    },

    placeObstacles: function(angle) {
        var obstacle = this.obs.getFirstDead();
        //obstacle.reset(this.worldRadius * Math.PI, 0);
        var ro = this.worldRadius * Math.PI;
        var x = ro * Math.sin(angle);
        var y = ro * Math.cos(angle);
        obstacle.anchor.setTo(0.5,0.5);
        // y grows going down
        obstacle.reset(x, -y);
        obstacle.angle = angle;
        game.physics.enable(obstacle, Phaser.Physics.ARCADE);
        this.rotWorldGrp.add(obstacle);
    },

    hit: function() {
        //playState.gameOver = true;
        console.log("HIT0");
    },

    jump: function() {
        if (playState.isJumping === true){
            return;
        }

        playState.isJumping = true;
        var jumpTween = game.add.tween(playState.player);
        jumpTween.to({ y: 70 }, 200);
        jumpTween.to({ y: 150}, 400);
        jumpTween._lastChild.onComplete.add(function(){playState.isJumping = false;}, this);
        jumpTween.start();
    }
}