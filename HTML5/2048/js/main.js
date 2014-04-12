window.onload = function() {
    init();

    function init() {
        if (window.DeviceOrientationEvent) {
            //document.getElementById("doEvent").innerHTML = "DeviceOrientation";
            // Listen for the deviceorientation event and handle the raw data
            window.addEventListener('deviceorientation', function(eventData) {
                // gamma is the left-to-right tilt in degrees, where right is positive
                var tiltLR = eventData.gamma;

                // beta is the front-to-back tilt in degrees, where front is positive
                var tiltFB = eventData.beta;

                // alpha is the compass direction the device is facing in degrees
                var dir = eventData.alpha

                // call our orientation event handler
                deviceOrientationHandler(tiltLR, tiltFB, dir);
            }, false);
        } else {
            document.getElementById("doEvent").innerHTML = "Not supported on your device or browser.  Sorry."
        }
    }
    // creation of a new phaser game, with a proper width and height according to tile size
    var w = window.innerWidth;
    var h = window.innerHeight;
    var tileSize = w < h ? w/4 : h/4;
    var tileScale;

    var game = new Phaser.Game(w, h, Phaser.AUTO, "screen", {preload:onPreload, create:onCreate});
    // game array, starts with all cells to zero
    var fieldArray = new Array(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
    // this is the group which will contain all tile sprites
    var tileSprites;
    // variables to handle keyboard input
    var upKey;
    var downKey;
    var leftKey;
    var rightKey;

    var scoreText;
    var score = 0;

    // audio
    var audioScore;
    var audioTilt;

    // true when the device orientation change has been alredy consumed
    var tiltConsumed = false;

    var tileNames = {
        2: "1.png",
        4: "2.png",
        8: "3.png",
        16: "4.png",
        32: "5.png",
        64: "6.png",
        128: "7.png",
        256: "8.png",
        512: "9.png",
        1024: "10.png",
        2048: "11.png",
        4096: "12.png",
        8192: "13.png",
        16384: "14.png",
        32768: "15.png",
        65536: "16.png"
    }

    // at the beginning of the game, the player cannot move
    var canMove=false;

    // THE GAME IS PRELOADING
    function onPreload() {
        // preload the only image we are using in the game
        game.load.image("bg", "assets/bg2.jpg")
        game.load.atlas("tiles", 'assets/tiles-atlas.png', 'assets/tiles-atlas.json' );
        game.load.image("lantern", "assets/lantern.png");
        game.load.audio('audioScore', [ 'assets/audio/score.wav' ]);
        game.load.audio('audioTilt', ['assets/audio/tilt.mp3' ]);
    }

    // THE GAME HAS BEEN CREATED
    function onCreate() {
        if (this.game.device.desktop)
        {
            game.stage.scale.pageAlignHorizontally = true;
        }
        else
        {
            game.stage.scale.minWidth = game.width /4;
            game.stage.scale.minHeight = game.height /4;
            game.stage.scale.maxWidth = game.width * 4;
            game.stage.scale.maxHeight = game.height * 4;
            game.scale.scaleMode = Phaser.ScaleManager.SHOW_ALL;
            game.stage.scale.pageAlignHorizontally = true;
            game.stage.scale.pageAlignVeritcally = true;
        }

        // create bg
        /*bgImg = game.add.tileSprite(0,0, 279,269,'bg');*/
        var bgImg = game.add.sprite(0,0, 'bg');
        bgImg.width = game.width;
        bgImg.height = game.height;

        var lantern = game.add.sprite(0 ,0, 'lantern');
        lantern.x = game.width - lantern.width;

        updateScore();

        // listeners for WASD keys
        upKey = game.input.keyboard.addKey(Phaser.Keyboard.W);
        upKey.onDown.add(moveUp,this);
        downKey = game.input.keyboard.addKey(Phaser.Keyboard.S);
        downKey.onDown.add(moveDown,this);
        leftKey = game.input.keyboard.addKey(Phaser.Keyboard.A);
        leftKey.onDown.add(moveLeft,this);
        rightKey = game.input.keyboard.addKey(Phaser.Keyboard.D);
        rightKey.onDown.add(moveRight,this);

        // sprite group declaration
        tileSprites = game.add.group();

        //	Here we set-up our audio
        audioScore = game.add.audio('audioScore');
        audioTilt = game.add.audio('audioTilt');

        // at the beginning of the game we add two "2"
        addTwo();
        addTwo();
    }

    function updateScore(){
        var text = "Score: " + score;
        if (! scoreText){
            var style = { font: "30px Arial", fill: "#ff0044", align: "center" };

            scoreText = game.add.text(0,0, text, style);
            scoreText.x = game.width - (scoreText.width +10);
            scoreText.y = game.height - (scoreText.height + 10);
            scoreText.anchor.setTo(0.5, 0.5);
            return;
        }
        scoreText.text = text;

        scoreText.scale.setTo(1.5, 1.5);
        game.add.tween(scoreText.scale).to({x: 1.0, y: 1.0}, 300, Phaser.Easing.Bounce.Out, true);

        audioScore.play();
    }

    // A NEW "2" IS ADDED TO THE GAME
    function addTwo(){
        // choosing an empty tile in the field
        do{
            var randomValue = Math.floor(Math.random()*16);
        } while (fieldArray[randomValue]!=0)
        // such empty tile now takes "2" value
        fieldArray[randomValue]=2;

        // creation of a new sprite with "tile" instance, that is "tile.png" we loaded before
        var tile = game.add.sprite(0,0, "tiles");
        tile.anchor.setTo(0.5,0.5);
        tile.x = toAnchorCoordinate(toCol(randomValue)*tileSize);
        tile.y = toAnchorCoordinate(toRow(randomValue)*tileSize);

        tile.frameName = tileNames[2];

        // this is actually a constant
        tileScale = tileSize/tile.height;
        tile.scale.setTo(tileScale, tileScale);

        // creation of a custom property "pos" and assigning it the index of the newly added "2"
        tile.pos = randomValue;
        // at the beginning the tile is completely transparent
        tile.alpha=0;


        // adding tile sprites to the group
        tileSprites.add(tile);

        // Add animation!!
        var fadeIn = game.add.tween(tile);
        // the tween will make the sprite completely opaque in 250 milliseconds
        fadeIn.to({alpha:1}, 500);
        // tween callback
        fadeIn.onComplete.add(function(){
            // updating tile numbers. This is not necessary the 1st time, anyway
            updateNumbers();
            // now I can move
            canMove=true;
        })
        // starting the tween
        fadeIn.start();
    }

    // Since tiles anchor is 0.5/0.5 we can use this hardcoded function!
    function toAnchorCoordinate(initialCoordinate){
        return initialCoordinate + tileSize /2;
    }

    // GIVING A NUMBER IN A 1-DIMENSION ARRAY, RETURNS THE ROW
    function toRow(n){
        return Math.floor(n/4);
    }

    // GIVING A NUMBER IN A 1-DIMENSION ARRAY, RETURNS THE COLUMN
    function toCol(n){
        return n%4;
    }

    // THIS FUNCTION UPDATES THE NUMBER AND COLOR IN EACH TILE
    function updateNumbers(){
        // look how I loop through all tiles
        tileSprites.forEach(function(item){
            // retrieving the proper value to show
            var value = fieldArray[item.pos];
            var currFramename = item.frameName;
            if (currFramename != tileNames[value]) {
                //update needed
                item.frameName=tileNames[value];
                item.scale.setTo(2.0, 2.0);
                game.add.tween(item.scale).to({x: tileScale, y: tileScale}, 150, Phaser.Easing.Bounce.Out, true);

                // scoring
                score += (value/2);
                updateScore();


            }
        });
    }

    // MOVING TILES LEFT
    function moveLeft(){
        // Is the player allowed to move?
        if(canMove){
            // the player can move, let's set "canMove" to false to prevent moving again until the move process is done
            canMove=false;
            // keeping track if the player moved, i.e. if it's a legal move
            var moved = false;
            // look how I can sort a group ordering it by a property
            tileSprites.sort("x",Phaser.Group.SORT_ASCENDING);
            // looping through each element in the group
            tileSprites.forEach(function(item){
                // getting row and column starting from a one-dimensional array
                var row = toRow(item.pos);
                var col = toCol(item.pos);
                // checking if we aren't already on the leftmost column (the tile can't move)
                if(col>0){
                    // setting a "remove" flag to false. Sometimes you have to remove tiles, when two merge into one
                    var remove = false;
                    // looping from column position back to the leftmost column
                    for(i=col-1;i>=0;i--){
                        // if we find a tile which is not empty, our search is about to end...
                        if(fieldArray[row*4+i]!=0){
                            // ...we just have to see if the tile we are landing on has the same value of the tile we are moving
                            if(fieldArray[row*4+i]==fieldArray[row*4+col]){
                                // in this case the current tile will be removed
                                remove = true;
                                i--;
                            }
                            break;
                        }
                    }
                    // if we can actually move...
                    if(col!=i+1){
                        // set moved to true
                        moved=true;
                        // moving the tile "item" from row*4+col to row*4+i+1 and (if allowed) remove it
                        moveTile(item,row*4+col,row*4+i+1,remove);
                    }
                }
            });
            // completing the move
            endMove(moved);
        }
    }

    // FUNCTION TO COMPLETE THE MOVE AND PLACE ANOTHER "2" IF WE CAN
    function endMove(m){
        // if we move the tile...

        if(m){
            // add another "2"
            audioTilt.play();
            addTwo();
        }
        else{
            // otherwise just let the player be able to move again
            canMove=true;
        }
    }

    // FUNCTION TO MOVE A TILE
    function moveTile(tile,from,to,remove){
        // first, we update the array with new values
        fieldArray[to]=fieldArray[from];
        fieldArray[from]=0;
        tile.pos=to;
        // then we create a tween
        var movement = game.add.tween(tile);
        movement.to({x: toAnchorCoordinate(tileSize*(toCol(to))),
                     y: toAnchorCoordinate(tileSize*(toRow(to)))},
                     150);
        if(remove){
            // if the tile has to be removed, it means the destination tile must be multiplied by 2
            fieldArray[to]*=2;
            // at the end of the tween we must destroy the tile
            movement.onComplete.add(function(){
                tile.destroy();
            });
        }
        // let the tween begin!
        movement.start();
    }

    // MOVING TILES UP - SAME PRINCIPLES AS BEFORE
    function moveUp(){
        if(canMove){
            canMove=false;
            var moved=false;
            tileSprites.sort("y",Phaser.Group.SORT_ASCENDING);
            tileSprites.forEach(function(item){
                var row = toRow(item.pos);
                var col = toCol(item.pos);
                if(row>0){
                    var remove=false;
                    for(i=row-1;i>=0;i--){
                        if(fieldArray[i*4+col]!=0){
                            if(fieldArray[i*4+col]==fieldArray[row*4+col]){
                                remove = true;
                                i--;
                            }
                            break
                        }
                    }
                    if(row!=i+1){
                        moved=true;
                        moveTile(item,row*4+col,(i+1)*4+col,remove);
                    }
                }
            });
            endMove(moved);
        }
    }

    // MOVING TILES RIGHT - SAME PRINCIPLES AS BEFORE
    function moveRight(){
        if(canMove){
            canMove=false;
            var moved=false;
            tileSprites.sort("x",Phaser.Group.SORT_DESCENDING);
            tileSprites.forEach(function(item){
                var row = toRow(item.pos);
                var col = toCol(item.pos);
                if(col<3){
                    var remove = false;
                    for(i=col+1;i<=3;i++){
                        if(fieldArray[row*4+i]!=0){
                            if(fieldArray[row*4+i]==fieldArray[row*4+col]){
                                remove = true;
                                i++;
                            }
                            break
                        }
                    }
                    if(col!=i-1){
                        moved=true;
                        moveTile(item,row*4+col,row*4+i-1,remove);
                    }
                }
            });
            endMove(moved);
        }
    }

    // MOVING TILES DOWN - SAME PRINCIPLES AS BEFORE
    function moveDown(){
        if(canMove){
            canMove=false;
            var moved=false;
            tileSprites.sort("y",Phaser.Group.SORT_DESCENDING);
            tileSprites.forEach(function(item){
                var row = toRow(item.pos);
                var col = toCol(item.pos);
                if(row<3){
                    var remove = false;
                    for(i=row+1;i<=3;i++){
                        if(fieldArray[i*4+col]!=0){
                            if(fieldArray[i*4+col]==fieldArray[row*4+col]){
                                remove = true;
                                i++;
                            }
                            break
                        }
                    }
                    if(row!=i-1){
                        moved=true;
                        moveTile(item,row*4+col,(i-1)*4+col,remove);
                    }
                }
            });
            endMove(moved);
        }
    }

    function deviceOrientationHandler(tiltLR, tiltFB, dir) {
        var f = tiltLR > 30;
        var b = tiltLR < -30;
        var r= tiltFB > 30;
        var l = tiltFB < -30;


        if (tiltConsumed) {
            // Game is ready for a new move
            if (!l && !r && !b && !f)
                tiltConsumed = false;
        } else {
            if (r && !f && !b) {
                tiltConsumed = true;
                moveRight();
            }
            else if (l && !f && !b) {
                tiltConsumed = true;
                moveLeft();
            }
            else if (b && !l && !r) {
                tiltConsumed = true;
                moveDown();
            }
            else if (f && !l && !r) {
                tiltConsumed = true;
                moveUp();
            }
        }
    }
};

