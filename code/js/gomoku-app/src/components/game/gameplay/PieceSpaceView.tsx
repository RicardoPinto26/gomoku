import {PieceView} from './PieceView';
import React, {useState} from 'react';
import {Box} from '@mui/material';
import {useTheme} from '@mui/material/styles';

export const CellSize = 40

interface PieceSpaceViewProps {
    rowIndex: number;
    columnIndex: number;
    piece: 'BLACK' | 'WHITE' | null;
    top: boolean;
    bottom: boolean;
    left: boolean;
    right: boolean;
    pieceSize: number;
    onPiecePlaced: (row: number, column: number) => void;
}


function PieceSpaceView(props: PieceSpaceViewProps) {
    const theme = useTheme();
    const {rowIndex, columnIndex, piece, top, bottom, left, right, pieceSize, onPiecePlaced} = props;
    const [selected, setSelected] = useState(false);

    function handlePiecePlacement() {
        onPiecePlaced(rowIndex, columnIndex);
        setSelected(true);
    }

    const lineStyle = {
        stroke: theme.palette.grey[800],
        strokeWidth: 1,
    }

    return (
        <Box
            sx={{
                width: CellSize,
                height: CellSize,
                position: 'relative',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                //border: `2px solid ${selected ? 'transparent' : 'transparent'}`,
                cursor: 'pointer',
                '&::before': {
                    content: '""',
                    position: 'absolute',
                    height: '100%',
                    top: '50%',//top ? '50%' : '0%',
                    bottom: '50%',
                    left: 0,
                    right: 0,
                    borderTop: lineStyle.strokeWidth + 'px solid ' + lineStyle.stroke,
                    //borderBottom: lineStyle.strokeWidth + 'px solid ' + lineStyle.stroke,
                    //display: top || bottom ? 'none' : 'block',
                    display: 'block',
                },
                '&::after': {
                    content: '""',
                    position: 'absolute',
                    left: '50%',
                    right: '50%',
                    top: 0,
                    bottom: 0,
                    borderLeft: lineStyle.strokeWidth + 'px solid ' + lineStyle.stroke,
                    //borderRight: lineStyle.strokeWidth + 'px solid ' + lineStyle.stroke,
                    //display: left || right ? 'none' : 'block',
                    display: 'block',
                },
            }}
            onClick={handlePiecePlacement}
        >
            {piece && <PieceView piece={piece} pieceSize={pieceSize}/>}
        </Box>
    );
}

export default PieceSpaceView;





