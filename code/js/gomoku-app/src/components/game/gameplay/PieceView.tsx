import React from 'react';
import {CellSize} from "./PieceSpaceView";

interface PieceProps {
    piece: 'BLACK' | 'WHITE' | null;
    pieceSize: number;
}

export function PieceView(props: PieceProps) {
    const { piece, pieceSize } = props;
    if (!piece) return null;

    return (
        <div style={{width: CellSize / 2, height: CellSize / 2, position: 'relative', zIndex: 1}}>
            {piece && (
                <div style={{
                    width: '100%',
                    height: '100%',
                    borderRadius: '50%',
                    backgroundColor:  piece === 'BLACK' ? 'black' : 'white',
                    position: 'absolute',
                    left: '50%',
                    top: '50%',
                    transform: 'translate(-50%, -50%)'
                }}/>
            )}
        </div>
    );
}
