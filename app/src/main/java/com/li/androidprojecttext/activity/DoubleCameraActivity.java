package com.li.androidprojecttext.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.facerecognition.dualdemoVL.ModelInit;
import com.li.androidprojecttext.R;
import com.li.androidprojecttext.databinding.ActivityDoubleCameraBinding;
import com.li.androidprojecttext.model.IdentityCardEntity;
import com.li.androidprojecttext.yansheng.BaseFaceRecognized;
import com.li.androidprojecttext.yansheng.FaceYanshenTwoRec;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreateDate: 2020/5/18 10:19
 * @Description: 双目摄像头的测试
 * @Author: 李想
 */
public class DoubleCameraActivity extends BaseActivity implements BaseFaceRecognized.FacecompareResult {

    ActivityDoubleCameraBinding binding;
    private BaseFaceRecognized mBaseFaceRecog = new FaceYanshenTwoRec();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_double_camera);
        ModelInit.initFras();  //初始化授权库
        ModelInit.initModelDirMap(getApplication());
        binding.btnRetry.setOnClickListener(view -> btn_retry_click());
        mBaseFaceRecog.setFaceCompareResultImp(this);
        IdentityCardEntity identityCardEntity = new IdentityCardEntity();
        identityCardEntity.setSex(1);
        identityCardEntity.setStrAddr("河南省息县临河乡柿树元村李东");
        identityCardEntity.setStrBeginDate("20100205");
        identityCardEntity.setStrIdCode("411528199302085479");
        identityCardEntity.setStrName("李想");
        identityCardEntity.setStrNation("汉");
        identityCardEntity.setStrSex("1");
        identityCardEntity.setEncodeBitmapStr("/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCAB+AGYDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD+/CiiigAoor4D/bt/4KQ/s0f8E+/BcOt/GTxO1x4w16x1WXwJ8NdBjS/8W+K73TrSScpHZi4hj0vTd/kQz63q09no1tcXNha3Wow3F/p4kBNpJtuyW7+9fp+W7d39b/En4u/C/wCDvhfXPGXxP8eeF/BXh/w7pV5rWraj4i1zTdLjttNsYZJ7m523l1AzpHHEzEr8oHzMwRXev5i/jz/wdDfBvwl8S9V0H4K/CXX/AB98O9LS3htvHWq3q+F7rxBdsrvcz6doGo6be6haadCrRRRvqkFjqElyLhTYLbRw3k/8wX/BR7/gpr8Yv2+vjVe/EnxJYw+BPDttpVroXhrwDoOt3+paNo2l2zyvJLcT3UVomp6vfySAXuqfYbPzLaDT4VtFkgmupfy6utUnm8wBiQfc84L9ME5yD69x1ORR/X9f1+JzSqt3UdFfR9XZy+7o93o0nfl1/uR8L/8AB1b8LX1q0Piz9nvxkPD24C+k0PX9Fu9WGZEAFpbX2naPaSllDbvP1G3RAVfzWZTE39C37H//AAUI/ZL/AG6NAuNV/Z5+K2jeJtX0y2huPEXgm6aTTPG3hsTS3lvE2seHL9YdRtLa6n0+/GmXcsCR6na2l1qFiJLFPPP+SZHqc8ULQljs4/iPAG/g5IHOW/XgYOfWvgr8ffit+z944034i/B7xvr/AIF8V6WES31vw5qU9hdSWovLW9ewuthMV9p1zcWNrJd6deRT2F15USXVtKilTnzrs/6+YoVZRvd8ydt91q9n6Jad7635pS/2LaK/i2/4JMf8HHnifX/GHh79nP8Abmj1TxPqfjXxbpfh/wCHvxr0+K1e5iu9evDaWWj+P7C2gs4kRb6S203Tdd0+H7NLDdWr+IIdNGn6t4kv/wC0SCeC6ghubWaK4t7iKOe3uIJElgnglQPFNDLGzJLFKhDxyIzI6EMrMp3G4tSWnS1/x8vJffvozrJaKKKYBRRRQAUUUUAeHftI/tAfDv8AZc+CXj745fFHW7bQ/CvgjRrm/muLhZ5Te6kYp00nR7S2tla5vL/V75YLKysbRJL27uJktrKGa7kggf8AyvP24f2u/il+2j+0T8Qfj58RrmKO88Val/xKdCsbie50rwp4ZtGlTRPDGlTXDtK1rYRebc3LottaXet3usava6Zp4v5rJP6qP+Dof9pbVJF+Ev7J/hzXLi0sPsE3xK8e6dbC/tZLq6NybTwnbXdxFKLLVNJlg/tK+k025SR7fV7HS9Qj8l4Iml/i7fSJmt3iiXIPXjsDIB3J/wAeO+TWUndtdnpvvdrv/ddv6b5K03eUVtH11et+vkresrt2RxtxeNMr8klugOf9vvkfl05HcHOci4yT17e3Le/cc/8AAsdV57+x8G3M0bP5P93sf9v647cHnpWmngG9MZkW3JAxggEd3HTBPp6nJPI+YmLve/zv5+vf8erZhSbnFN6ttpdPtSiv/bf892eY/Z/MDDb6Z568tjofU/XOOcg5mS1WNCvIwBnjsDJ1zyfx6dMmuuuvDeqWkjKICuM55bnBcf3cevcYOOO9RLpjTwTIV/0tGClOev7wkZ3Z7g8j0yTkmiNpdbLv/wCBLv8A3fx11VypKS5la8o9Oj1l59o3379VrjWRjBhVQuY54JSSquHWOV2aN0fKFJAArqylSrEMCDX+iF/wQA/4KbzftW/COL9mf4oXER+LHwa8M2MWja5PfPNe/EDwhZkQf2jJHdXd3qN7reiRtAPFmpXLtbSXl3Yagl2j6zDoVl/no2ukTQHZOpSXgYHrlx04J6L/AN9A4JU195fsBftK+KP2P/2lvhh8b/D095GvhLxBa3PiTT7N3Zte8JXEiWviLRpLFtQ0+y1KWWwLX2j2uqznSofEllompXkMq2YFaRjKLdtU7a6aq8tbX8tNej6No7sPrRad3JWd7N78ztdN9W936Xs2f6udFYXhfXYPFPhjw54ntomgt/EWhaPrtvA773hg1bT7e/iid9ib2jSZUZ9i7iGbYuCDu1oUFFFFABRRRQB/n5f8HAnjew+In/BRX4gWVtaXttD4C8F+CPAd99q8po7nUtOh1i/uL6x8t2Js7mLVLeIeeI51ube5UxNAtvdTfjp4Q+E8viJF/s+1Mqvsx8nXmTH3Q+fU891BPGT+1v8AwcGeD9J8Nf8ABRTxRD4etntZPGfgP4aeKtccTTSvd65qja9p13OiSOyQrJbabZoltbokJdJJ3R7ya7uJe8+AP7PPwo+E/wAMfCmu/FOW0sLi80nSbm4ubhLXzRc3ULM6Ey3NuMRysY2JwQVO4bt1eBm2LlhoqjTlavUTnBJ2uuaSu7O+97q2jcVqnI7sly2OPxM3WcfYxqKnOb0ipc0nu0ltF363drNOV/hT4U/sZW3iSz0yGfTkE02j6nNcNJEAp1NolSxhd2wqRRyRhxI2CvmyFsqik2fDf7HWpNq2n6dqmkKsU0ZM+1C+1klC8/IB0c/X3r99PhP4W+EerWcmp+CfFWgalYXex0hOqaRHdQI6SKFhtYb6WR49qlmLZ2tkEjdx3l58M9DXVxdW7RzAbsPEIpBy7kAsshAJ25xnoRyduT8LiM3zug5Ko0rN296d3bfa19tr6Ju7bSkfqOA4VyCSjZxk1azhGEo6yl1Uuij3veb3adv50/i3+w5baUl3d6LbHdE+7yPKijzHukLEABm4AJx9ecjdXxD4i/Zf8Tabcf2pp2ivNBLkymKKVpRMzuIz5aRMdu1VLsTgADJIzX9eXiH4Y+F71p5dWuLW2iZXV3uZLeJVD+YDlppUA4BOCc4zk8ZPnsfwu+BHh21e+1zxJ4eTSUXEg/tDR3lkDGQDy4f7QVpOEcfKfvFFBJLZvBZ7nCXtI03WiuX3b1HdXb1STe26va1k1e0jTGcF5E4Tcq9OktPebpwndXv8UrPpZN7t7pa/x5+MvhteeHFS41Wxngmxtl/dSbVkMkigbyqZ6Z5wc54wSTzOh6VDJdRszskRBDyJy4Usc4BLckDg/N7BsEH+l39pX9mn4NfF/wAI+J9R+FlzZ3Xl2009m0aWcdwtxCJruA+ULu4KpIIvJlfkRxSyHG4g1/NJb3d5pmval4fvbdrO60m8udPv4GRw0N3azSRSLtkCkAMjFSQDggkc8/c5Pm08fFQxFL2FZpfu+WUettVJp+em3NZ3bTPzDOsoWW1JvCVnVw94xVRSWr5koW5Xreztq2tH25v9RL/gnL8bn/aG/Ym/Z++Jd3LBLq954D0PSfEJtLCXTrCPXdFsrawv7bTrSeSW4SxtGjS2t3uJJJpVQStcXKsL2X7Zr8df+CDerPrP/BM/4P3DgA2uueNtMUjJ3x6brjWUcjZJIaRYVdxnAYsFyo4/YqvecXFuL3i2n6ptPv2/FdUzyopqKUt0kn6++n+n/DptFFFFIYVk6/qUmjaBrmsQwG6l0rSNS1KK2AYm4ksrK5uUgAU7szNAEAU7ssQDkZOtTXRJEeORVdHVkdHUMjowZWV1YEMrAsGU5BDMCCCSyezSdnbR2231t92n+bKg4xnFzjzxUouUbtc0VKTlG+65otK+69Uf56f7SeqeMf2rP+CgHhzxH47hkubz4gx2etx6Jd3DXn9gaPomoam8Gj2k0sFvM9pbrZhtjwxIJ7m7uY4Y5Z5y37TeOPgn8PtZ0Syg8fXVklhaW3kRRXzWqrbwGWZnQCaeLbHnLMWPAzluM15b+0b+y0vwj/4Kr+C9WdFbQ/Evhn4iat4dvJJYTIWv7ltfliaNY4THLDLqmpW6IPOjWxSyBupr77eiffvi74LeHPiPaM2tWaXY7RS26TIctI3zK7YIz/CwKtyCDg1+c5s51Mzw/PiavPhqEqVSpFRi3ONV3binyw02W1rNtt3X7TlWU5PThjsfluBjTwOZ4iONwmW1KtTEU8LRnBctCNeslWrciT5p1Pfc5Scm2kfjL4z+A37LXhXxCmqeCPiF4a03xHFg2dro3i/Tk1WLzmDxFdNh8QtLErDy5FCwbSrq4yrA19Y/A7UPHj6bdWR1i98QRwFdl7f3byzkJFIw3SBZC+45JZnLdPmPUc38Zf8Agnf8LfFnxR0PxzbeGktNZ0TSrTSYYLGCO30+5NkY4or65sox5Zu/Kgt4o2j2RxJEgRCdxP3r8BPhDZeB4LbT72Ap58Ba4V4kH71VkjUHBXqG7+uCOM15GPrKc5wVWdZO15TtfSU91d67fJtXaWvs5Tgub20pYanhkqiVKFKLUeX37NXvre9lr0eklJn53ftCeIvFEVhc6VqZmtFvJFVZo2d8AyTR7jvVVVPnJJJwBkluGz8heCf2e/gDresQaz8RfHGif2iyuZLTUPFVjA7u5G4fZH1y32jcigxrCFOSCpO4V+0f7Sn7PWnfEK4le1vr2xitba5g+y2UUQS6jd5XaRpH3vHIoJRHTO0HdhjzX5y6D/wT++FV54+0rxTqfg2G51HR9Nv9FYalJ9tg1iLUZFaa/wBVW5j23N/EFCQXSCNokI2jcrsccBVlhpyn9YqRScbQuuV6y2TaWul07rRNu716MblUZuU3hoV3f+HUV4yu9dLXvovKzejV7+z/AA6+Dfws0ATN8OdR0uWyuoZIZLfT7iwkhaCWOSFiPs91cFsRE4AY53AEnAJ/m+/bs+Ar/DD9szxFpUKSWujfES7n8W6dMsSoHiuntYZIrdCNkr/azcLImWkD7QSGbaf63/Av7Nfw/wDhnplvL4V0Oy0UxQhXtbCIeQDl1++0sjHCqAD6E5z1r8V/+CvPgsan8Sf2eddsrYtq1outaZbzwIz3KJHq1nqUO0KWIVZ4uTtwFmck5Ck+pgcdiKmZyruU/ZezlZw5m+dSXKtrJXa69+jR83neSwr4DD04YWGHTxmG5oUV7qi5vmsmm9tUrW2vdo/Zf/ghr8X9f+Gxi/Yh1rRroaRpfgSX4l+FtZE8U0DXGqatfX3iJrx3vp5Ue7ubiOPTrS3tYYY4Ibo3E6RpplpX9I1fgz/wRt/Z11+DSpP2m/iC0smv3fg6x+H/AIWZ5Zmb+y4p5b3VrmQ+YYJd81zHpxt5rS31DTpdP82C5uNO1Nwf3mr9FwFWpXwyrVXJuo3KLmrScbyV2kurV15ct2223+f8W0MlwubRw2Swrwp4fB4elj/byi+bNFOv9alR5JSSpKLoxUdGpqrzRck5MooorrPlwooooA/MD/gof8JLrWdR+EPxpsFH/Fv9abSNUMUUSyJZeIpDppu5p3dCUjS5eKOBd8s1w0PzRQxS+Z4vpPi94V2RyfKcZG/5cguOME57Ht1PJxk/SH/BRr9tj4E/slfB+9074mXq6x4o8dwS6V4V8CaZNaNr2pfMou9UdJ5UXT9K06KRXvdSnKpGJYLaDz9Su9N0+4/OzwR4ht9W0/T71biGe3vYI5raeBwyTxMzhZQBJIAGzlf3jDGGDEHNfFZ9goKvWxFN6zjCVRJ7SXua3enNGC0WqbctUm1+tcCZl7fDQy2tG/1eVT2LS+Ki5ufvar4akp9XeNrtKMr/AEZda3At1JqlxLteNJCGB/jIZl/j7lRz9OMgk09I8U3mrXEl6zhhnfC5k4MXzFiTk4IK9OeTnORXLzjS7myuILqdUQxudzFApYLLtyWkA5OP044cV8f+OdZ+JWhandW/hHxhBHbiG4XTNKhmihiMQDqouLz7VMsTB/4fsykoQcgjJ+PWGlOcuV3T6Ja/b7Kyv9yu7u9m/wBOjOjCpaDskuj10ut9dPf6vS78z7p8XeJrzT7Y3kXl3Blsp5ysUhkKwoJvM8wRMxRsAnacfLkk4PPh+g+NtN1+N7yEmKdGVTHgjltx6u+7+Hp0HTBJzXwb4b+IXx+HjKyt/FXjO00zTkimttQ0aKc6lbalG8gWQyX63tkIF8tmhKfZ5Syuz+YhGG+qvDtho+l2clxBeW8rMyMXjlhYscOMko5z0X26epY39SlfWM7eSTf5/wBbW6nT7WjFtOpKytdxf+Jfot9ve7a+33fjf7LbS2sk2MkZBcj7pkAyN3uef5Ec/Efjj4R3n7Q37Sfwy0eLRW8QW+m6L4jlhieC5uIdPu7/AFHS7K31a7FtDM9rY6fJJEZr50MMAuD5gZiin1jWdSW6uJCsmSdxHI5AZ+eGyOh/McnGa/Uj/gnL4b0STwz478XfZY5NbGsWOjLdsAzRWS6eLoRxKxcQv5lxOGmh8uWWKZ4bgyRpCq+tlGDrVcXTw8ZzpQldyqcqbUYKctE9Lvl5dWlaUb35Wn81xTm9DL8oxGJpN1K1GdH2EW1rUlVjBOfvXSinzPRttctlfmPuH4N/DTR/hB8M/Cnw90WPZa6DplvbzSFYUlur4puvLy4FuFt2uribMl1LBHHFcTs9wIkaRxXptFFfpcYqEVCO0Uor0V7f16avU/nerVqV6tWvVk51a1SdSpN7ynOUpSk/OTd/u10dyiiimZhRRXlXxz+IOmfCf4L/ABR+JOsag+lWHg3wT4i1ubU0jeV7F7XTbz7NdCOM+Y/k3PkSER5kwDsVnCqXFOTSWrbSXm25JfjH8Vu7ibUYuTdlFNt+S5rvf+736rV6s/hV/wCC23x30r9oP9r34ia14WRH0b4brpfwzhut1yDfT+F7jUHvrh4541Fsf7RvLuxeGFpYme1a8juCtysKe5/8E8P2rtB+Ivwu0zwdq2ux2/jLwHEmg6hp9xcRpNdW1pA9xa3Np51wst2otiiTGGHy4X8uIsWOT+Jvj/4gX3izxT4pvfEd3Nd3XjLWNX8SazqEcS7r7xBqd7dX11dPDFHDBG95fXc0xjiSOKMyMkMKxoEPnPwgV/hN8cfBnxEi1O80qxh1qCy8RpbzNazXOm6hKYLppRHLFLIFUR7ot6xMoPmAqGNefneAhTw1TVSqX1fLa9m+t3fVrpe7V9D1+Fs3dHGQxNLmhHl9nK7to5JNb27t2v1V7Nt/1O/GTx/8W7+axT4bzQXFvcFftNvd6kbFebkryoilY/uyc5PfBI614lNffH2yu5JrzQvCUz5JljuPFN55hbLZ2tJpVrawjC8q13LyynzcZrvvAPinSfEmn6Z4g0rVU1PT9RiSezvFkhfMbO6gy+TI6RMzLkKWzgZyRmuc+Meg+M/Ei3tto/iK50u2mLYmsp7YunMoXaJEcdCTyTjgZJLV+XurUoV+R2UU0mk2k3eWrtfqlddNuZ2k3+65ZjcPiYqrzptpXd11bvZXbuku9n5tq/lniCf44avetJb+GPA2nu+/bPc+MEmLAtn5Bpn9ovz/ALSj5uuB81c/o+rfH3w14jtpfEGvW8Gio3ly6Xoc091C7NJtRjPKVkdE5BLWkZKk5APNa/w1+EnijQJYLvV/HGp6uke3zf7RvLZtx3H7sW7bCG28xwhEzzjcST9A6zHoMVrM0cscuobSqRKFZSD5gY7llJHOzt3GT0zniMfKlGcubRW223aXXd6de71Z1YytCTlGi25Ntpa6JPXZttdV0W2u4zXvjLoPgnwjqPjPxfqMGnaZYQPJNcySxrKx8q5kWGKKWWDzJZhAyxxBt8jhUUliSfmz/gm3/wAFtvFngP8AaZ1nw/4vFte/AX4l+MbLSjpl1cw2T+DYJLiHR9L8Z22o30sSw2Jt1guvE2nXkrixga41LSZRJaalo2p/IX/BSjUo7b9mXU9H+0RtrV/4s8JzW9m8igxWkd7ex3CxDcXKgsrYKHlvvDFfjh8DdO1my1bT5vLRBaNHI2+URkpFJ5jJHvKhpWEeI0JBdjjII5+j4VhXxdGpi02pwqx9m23rBprXdSTtZp6Wa1cnc/KeK80lGbwUuWdGdP8AeKpK1pqo3Fx0bUo8t9LNNK7a1P8AXssb211Kxs9RsZluLK/tbe9s7hAwSe1uoVmt5lDqrhZYmV1DqGAOGUMDVqvG/wBnbxhofxA+Afwe8Y+G7iS70TXPh54SudOupYXge4gi0e2tTN5Mn7yJZZIHdEmCThNonijmV4x7JX6Ar9dH1Xn7yf5L8d2mz4BWautVpr5a269eV/5vqUUUUAZWt67onhrS7vW/EWr6domk2EUk95qeq3lvY2NtDGkkjvLc3MkcagJE7YLbiAcAk8/zVf8ABYz/AIKpfsneLf2c/Ff7NfwT+L9t498e+OLjRjqGqeB1u9U8G2fhqy1GSbXNN1XxLaRyaZef2tawy6dPpFtLczOk9u19bDSro3Z/lQ+Nn7Y/7R3x91HxNf8AxW+MfjrxlH4p1i71zWdF1DxFfW/ha41G71KbVWaHwnpsln4bsrW0viJdK06x0m307R447W00a1s7K1tbdflDVNQNzHHJLKbWWArHHKhBJDSMTCWY8JKeG6nGCDkEj1KGDjTkp1JuUotSiopKN021e92932e2qbkzwcRmM6sJ0qcFCM04uUm3OzdnazSV1fv0V3uuqvrl5roBbmRYUcSJOAu/zo3ZosAnbgsM8ZI+Xjkkrqd5b6jaSy6l/pl+qNC0UoLC5aQSg3LlWDLLGcbQAFO5c52sW5y0vPtlnLldjWksSzAsQpkBkdTEzH51x1YHGeMg81XuL+3G+6VwbsfdAxwcvnJD5AJPYY69SKWMw31im42Tb3vu1fzfkpfrdXemBrywsFFyttdrZayXfdK3Xd7tq59t/ss/tKeIfhJb2fg/Xb17nwzcJGthaPOzDS0QPDDHHbxlSirIzSnzWJUkEnaxz+i9x8U9a8XafcTeEvHb2iEZjEj20luFxMeUF1CwOM4bzsDcSwOFz/Pgl7LNdPey3skU7ZzF8ojIYuD87Nnjn8zzkDP0V8OvHuoWWh3A03xPo8DabJFbXmiXGpGDVrtWSV3uNOthA8V1aQRjbNIbiORZGKLG5Br8uznh6rTqVMRGpVSTS5GlyLWbvZLmWq7tWvJf3v0nIM9lCNOHPdTdKEdd3eSTWu93b8L3dz9StD1X4jXOp7dS+KUk0KNiWwsktAGbcSD5rahckhR2EZJBIBByT2PjP4s6H8MtDvtY1zWZJbm2t5Wi84x+c0ojuNmFSVWO5405xxxwT1+H9J+InhTwt4PtfGGqapHYXuqoZNPjuGihurh1G3fHDLOkzxmUxIJUVl37kBLKwr5Z+J3jfW/i/JNvuzLax53LNKFixvkkG5pBx19e/XnB+fwWTYrGYlU6lP8AcSspJptt8zd7Wsu929m3e12/o80z2WEpVHCbVaOm7uleXXmvtZ9UlfVtI8u+N3xs8bftCeNL+9v9Uvf+Ecs7wnTtPdma3MIlFxHNslTcjRSbyoDEZzkk1N4fa2gj05InKvayefPKOHu5FaMw+cCxUeSYiUK4P75y27Ckc+thp3h6yjtbWRpr4yPFdIiKbe3iwqoLaeNz5yuWlL5HyhV5OcB3nQ2MDSbjtDIu7GDklsc7s+mOc8kZPNfsOS5PhsuwnsqcNE42TXLblbs7J9bbPpZ7/F+U5nmc8e5VKkr1W1d+V5/3m1vfpZta3uf3Xf8ABJb/AIKzfsu698BvhF+zT8TfFll8LviV4B8PaX4K0z/hJJZLfw54rismtbOwuNO8RTCOy/tXVbm8ggg0WXydRe7Nxb2lpc2Fn/bVx/QVo3iDw/4jtje+Hdd0jXrRSFa70bU7HU7YMdwAM9jcXEQJ2tgF8nDckgmv8jQ+KGhvUgt2lNw2WiIVtmVdsHfyBg8j23Z5r6X8DftM/HX4ZaRFb+D/AIr/ABC8Jwxyrcmw8OeOfE+i2P2oAIbgWWnapa2wuNkECrcCLzlWOMLIPLGe+eCi3KUJ2u72aur3lfW91vfW/RX0bfBSxkoRUZxU0rJNOzsua19091r2ve7bP9Uuiv4IP2fv+Dgn9uz4Y6Fc6drep+APi/EQsdrJ8VtC1S9udPjC2yRR2Vz4O8ReCrjy7ZbeURJdS3CkX10JxKItN8grB4Osm/hfZ82+/dJ9vvavpd9CxlFrXmT7ct+/Z+n3tdLv8I5LUx5yMgdeCMcv6n27e3B6ViXcCXZktpoImVlYxyOxBWVfNEcgydu5GIZc9wckgZrrsmX7/OT7+rD1Hr/LnIJODqtuqh1B4DYHB/6af7WfzJ+pr1T56h8a+f8A7kOdtdVbTrabSb/5pYlIgvXJxdBFl+d3yIhK7EKiL94A4JYVz0dy8ku4gqc8hgQcZfOMk8fLyfcck9NDVYVubHy5z5kcAURIRwjhpGjlHOQ0bfMvUZ6iuRje4hIEtxJcscHzJMBuC/ZeOcc8dCOu0UHTL+HU9P8A26oauqahZxRSxzrg95ow7yrzL0UNjtk5z25JPPl9t4jTSfEUN5YvNdrEWaRJkaMyRCQM67VO4llXGF5OSuSTmu4urVLssZW+96DpyT3PTp7+5yau+BfCGgat4xsrXUYnWPJfzYIUkldo7iIrE6yTIvlSkYlYNvC52gsTnjzPDwngqjlrvbTzmu/92/3Lo2+nKsROOIpRW0JQmtWtYSduvp/k7s9B+I3jTwhp+q6Z4o1u70j4h2vhiR7Dw/4B1iJptL0+1nhF15d1NBcRyWr2FzcRXSWjxXCS3CTRSzDZLEPH7b406rrV7NdRW9hollc7jcWGkCK3tmLh1zHZQxRRRKqZVUijROWJUE5rr/jB4T8Hav4e8K+JNF099FudRuPGNnq+nw28QsRd6VqNhIlxaTG5mnmFyNSdpjOsTLIMIhUk186aX4cs0dTGxjbjDqi5HL+pxnjr7/XPh5bhqdJRirvm5bvRaJJ2XZO3m99U25P38yxdavWmpyvezer6ua+58t7a9FpZt+/L41sFhzD50nlgBVkhdFwS+MkOQOnTGcEddpq1J4hbWbaC2toQskgR3UB/JEiPJgGQ5HP59cZwTXmttaeWptnmeZTjLOBk4Mg5AOPf/CvS/DVjDbRoqcj5Oo95AO5/mc+uea+ip/F/X/Tw+c+1/wBvf+3HYaNoS2kDalfsJLslWRRh0tx8+6OJ9ynaSA3zZOc8kBs519qN5eXskY5tlJWQ7m4O5yB2HIycE+nJwxrq9QJttEeSP7xmt04yOHaUE9fT/wCuScEYq20cNtEy8teIJpTjHzhnTseeFHX9OSdgfxSIraX7MhjgOU47kdC+M7WI5/nkZyCSVPDaRhM5PzY9eMMw7knv+p5xRQSf/9k=");
        finishScan(identityCardEntity);
        mBaseFaceRecog.onCreate(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBaseFaceRecog.onResume();
    }

    @Override
    public void faceCompareFailed(String failedReason) {
        binding.txtMsg.setText(failedReason);
        binding.btnRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void faceCompareSuccess(float similarity, Bitmap img, String idCardNumber, long requestTime) {
        //比对成功
        binding.resultImg.setImageBitmap(img);
    }

    public void finishScan(IdentityCardEntity identityCardEntity) {
        String scanIdCard = identityCardEntity.getStrIdCode();

        mBaseFaceRecog.updateIDCardPhotoBase64(identityCardEntity.getOriginEncodeBitmapStr(), scanIdCard);
        mBaseFaceRecog.updateIdentityCardEntity(identityCardEntity);
        binding.txtMsg.setText("请正对摄像头");

        Observable.just(true)
                .delay(2, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.PAUSE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> continueCapture());
    }


    protected void continueCapture() {
        mBaseFaceRecog.continueCapture();
    }

    void btn_retry_click() {
        binding.txtMsg.setText("请正对摄像头");
        binding.btnRetry.setVisibility(View.INVISIBLE);
        Observable.just(true)
                .delay(2, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.PAUSE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> continueCapture());
    }
    @Override
    public void onPause() {
        super.onPause();
        mBaseFaceRecog.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBaseFaceRecog.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaseFaceRecog.onDestroy();
    }
}
