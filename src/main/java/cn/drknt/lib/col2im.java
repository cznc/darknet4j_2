package cn.drknt.lib;

import cn.BaseFunction;

public class col2im extends BaseFunction
{

static void col2im_add_pixel(float[] im, int im_offset, int height, int width, int channels,
                        int row, int col, int channel, int pad, float val)
{
    row -= pad;
    col -= pad;

    if (row < 0 || col < 0 ||
        row >= height || col >= width) return;
    im[im_offset+col + width*(row + height*channel)] += val;
}
//This one might be too, can't remember.
public static void col2im_cpu(float[] data_col, int data_col_offset,
         int channels,  int height,  int width,
         int ksize,  int stride, int pad, float[] data_im, int data_im_offset) 
{
    int c,h,w;
    int height_col = (height + 2*pad - ksize) / stride + 1;
    int width_col = (width + 2*pad - ksize) / stride + 1;

    int channels_col = channels * ksize * ksize;
    for (c = 0; c < channels_col; ++c) {
        int w_offset = c % ksize;
        int h_offset = (c / ksize) % ksize;
        int c_im = c / ksize / ksize;
        for (h = 0; h < height_col; ++h) {
            for (w = 0; w < width_col; ++w) {
                int im_row = h_offset + h * stride;
                int im_col = w_offset + w * stride;
                int col_index = (c * height_col + h) * width_col + w;
                /*double*/float val = data_col[data_col_offset+col_index];
                col2im_add_pixel(data_im,data_im_offset, height, width, channels,
                        im_row, im_col, c_im, pad, val);
            }
        }
    }
}

}