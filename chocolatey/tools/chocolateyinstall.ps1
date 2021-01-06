$version = '2.2.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'CFB40DA0D474F9AD685DDD9D96528C45ADF29CD9BB900CF1179DFDD6541AFCDC'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
