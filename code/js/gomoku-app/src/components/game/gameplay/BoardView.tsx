import React from 'react';
import {Box} from '@mui/material';
import PieceSpaceView, {CellSize} from './PieceSpaceView';

interface BoardViewProps {
    board: Array<Array<'BLACK' | 'WHITE' | null>>;
    onPiecePlaced: (row: number, column: number) => void;
}

export function BoardView(props: BoardViewProps) {
    const {board, onPiecePlaced} = props;
    const boardSizeInPieces = board.length;
    const pieceSize = CellSize

    return (
        <Box
            sx={{
                width: (boardSizeInPieces) * CellSize,
                height: (boardSizeInPieces) * CellSize,
                margin: 'auto',
                position: "relative",
                border: '3px solid lightgray',
                backgroundColor: '#ba8c63',
                alignItems: 'center',
                justifyContent: 'center',
                p: 2
            }}>
            {board.map((row, rowIndex) => (
                <Box key={rowIndex} sx={{display: 'flex'}}>
                    {row.map((piece, columnIndex) => (
                        <PieceSpaceView
                            key={`${rowIndex}-${columnIndex}`}
                            rowIndex={rowIndex}
                            columnIndex={columnIndex}
                            piece={piece}
                            top={rowIndex === 0}
                            bottom={rowIndex === boardSizeInPieces - 1}
                            left={columnIndex === 0}
                            right={columnIndex === row.length - 1}
                            pieceSize={pieceSize}
                            onPiecePlaced={onPiecePlaced}
                        />
                    ))}
                </Box>
            ))}
        </Box>
    );
}

