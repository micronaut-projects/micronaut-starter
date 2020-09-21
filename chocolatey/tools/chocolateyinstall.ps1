$version = '2.0.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '8D299165224F805A673F219CA3EEE598AA52D871C79E1443552580D9659C59CE'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
