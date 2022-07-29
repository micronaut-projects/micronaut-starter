$version = '3.5.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '745B15761FF29FFD049C3F7EF5D41D38CEBAF0016A468A75C2275099574DC26B'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
