var playState = {
    create: function () {
        // sky
        var sky = game.add.image(0, 0, 'sky');
        sky.fixedToCamera = true;

        //  Activate the Ninja physics system
        game.physics.startSystem(Phaser.Physics.NINJA);

        // Map tiles
        this.map = game.add.tilemap('map');
        this.map.addTilesetImage('kenney');
        var layer = this.map.createLayer('terrain');
        layer.resizeWorld();

        var layerObstacles = this.map.createLayer('obstacles');
        layerObstacles.resizeWorld();

        // In Ninja the Tiles have an ID from 0 to 33, where 0 is 'empty', 1 is a full tile, 2 is a 45-degree slope, etc.
        var slopeMap = { '32': 1, '77': 1, '95': 2, '36': 3, '137': 3, '140': 2 };
        this.tiles = game.physics.ninja.convertTilemap(this.map, layer, slopeMap);

        var obstacleMap = { '124': 1 };
        this.obstacleTiles = game.physics.ninja.convertTilemap(this.map, layerObstacles, obstacleMap);

        // add player
        this.sprite1 = game.add.sprite(50, 50, 'ball');
        game.physics.ninja.enableCircle(this.sprite1, this.sprite1.width / 2);

        // Camera
        game.camera.follow(this.sprite1);

        // inputs
        this.cursors = game.input.keyboard.createCursorKeys();
    },

    update: function() {
        // collision detection
        for (var i = 0; i < this.tiles.length; i++)
        {
            this.sprite1.body.circle.collideCircleVsTile(this.tiles[i].tile);
        }

        for (var i = 0; i < this.obstacleTiles.length; i++)
        {
            var mTile = this.obstacleTiles[i].tile
            var collision = (this.sprite1.body.circle.collideCircleVsTile(mTile)) !== undefined;
            if (collision) console.log("collision detected!");
        }

        // input movements
        if (this.cursors.left.isDown)
        {
            this.sprite1.body.moveLeft(20);
        }
        else if (this.cursors.right.isDown)
        {
            this.sprite1.body.moveRight(20);
        }

        if (this.cursors.up.isDown)
        {
            this.sprite1.body.moveUp(20);
        }
        else if (this.cursors.down.isDown)
        {
            this.sprite1.body.moveUp(20);
        }

    },

    render: function() {}
}