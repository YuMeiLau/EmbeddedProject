clc
MAX_NO_DRONES = 10;
sizedroneVecTransposed = [3 MAX_NO_DRONES];			%write transposed matrix for fscanf as it fills output array column wise
formatSpec = '%d %d %d';

while 1
    inFile = fopen('droneposition.txt','r');
    droneVecTransposed = fscanf(inFile,formatSpec,sizedroneVecTranspose);        %fscanf reads the file data and fills the output array in column order
    fclose(inFile);
    droneVec = droneVecTransposed';						%get the correct matrix
    x = droneVec(:,1);
    y = droneVec(:,2);
    z = droneVec(:,3);
    plot3d = scatter3(x,y,z);
    xlabel('X in mm')
    ylabel('Y in mm')
    zlabel('Z in mm')
    set(plot3d,'XData',x,'YData',y,'ZData',z)
    drawnow
    pause(0.05)
end
